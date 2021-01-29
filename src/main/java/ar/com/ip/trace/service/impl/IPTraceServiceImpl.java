package ar.com.ip.trace.service.impl;

import java.util.List;
import java.util.stream.Collectors;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import ar.com.ip.trace.dto.IPTraceResponseDTO;
import ar.com.ip.trace.dto.StatsResponseDTO;
import ar.com.ip.trace.dto.ip2country.IP2CountryResponseDTO;
import ar.com.ip.trace.dto.restCountries.RestCountriesResponseDTO;
import ar.com.ip.trace.entities.IPData;
import ar.com.ip.trace.exception.IPTraceException;
import ar.com.ip.trace.repository.IPDataRepository;
import ar.com.ip.trace.service.CurrencyConversionService;
import ar.com.ip.trace.service.IP2CountryService;
import ar.com.ip.trace.service.IPTraceService;
import ar.com.ip.trace.service.RestCountriesService;
import ar.com.ip.trace.utils.IPTraceUtils;

/**
 * @author Leandro Coello (leandro.n.coello@gmail.com)
 *
 */
@Service
public class IPTraceServiceImpl implements IPTraceService{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
		
	@Autowired
	private IPTraceUtils ipTraceUtils;
	
	@Autowired
	private IPDataRepository ipDataRepository;

	@Autowired
	private IP2CountryService ip2CountryService;

	@Autowired
	private RestCountriesService restCountriesService;

	@Autowired
	private CurrencyConversionService currencyConversionService;
		
	/**
	 * @param String ip
	 * @return IPTraceResponseDTO
	 * @throws IPTraceException
	 * 
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public IPTraceResponseDTO getTraceData(String ip) throws IPTraceException {
		
		//Se obtiene el pais correspondiente a la ip mediante la API
		logger.info("Obteniendo el pais de la ip: "+ip);
		IPTraceResponseDTO ipTraceResponseDTO;
		IP2CountryResponseDTO ip2CountryResponseDTO = ip2CountryService.getCountryByIP(ip);
		
		//Se recupera la informacion desde la  base de datos
		logger.info("Obteniendo el pais desde la base de datos");
		IPData ipData = ipDataRepository.findByIsoCode(ip2CountryResponseDTO.getCountryCode());
		
		if(ipData != null) {
			
			//Si se encuentra en la base de datos, se completa el DTO con los campos no persistidos
			logger.info("El pais se encuentra en la base de datos");
			ipTraceResponseDTO = createDTO(ip, ipData);
			
			//Se incrementa la cantidad de invocaciones 
			ipData.updateInvocations();
			saveData(ipData);
			return ipTraceResponseDTO;
		}
		
		//Si no se encuentra en la base de datos, se obtiene la informacion desde las APIs
		logger.info("El pais no se encuentra en la base de datos.");
		RestCountriesResponseDTO restCountriesResponseDTO = restCountriesService.getCountryData(ip2CountryResponseDTO.getCountryCode());
		long dist = ipTraceUtils.getEstimatedDistance(restCountriesResponseDTO.getLatlng());
		
		
		//Se crea el dto a retornar con las respuestas de los servicios
		ipTraceResponseDTO = createDTO(ip, ip2CountryResponseDTO, restCountriesResponseDTO,dist);

		/*
		 * Se persiste la informacion necesaria dejando fuera los campos:
		 * ip: se utiliza solo en el contexto de cada request
		 * currency: solo la moneda, el valor puede cambiar en el tiempo
		 * times: depende de la hora de invocacion 
		*/
		saveData(ipTraceResponseDTO,restCountriesResponseDTO,dist);
				
		return ipTraceResponseDTO;
	}


	private void saveData(IPData ipData) {
		ipDataRepository.save(ipData);
	}

	private void saveData(IPTraceResponseDTO ipTraceResponseDTO, RestCountriesResponseDTO restCountriesResponseDTO,long dist) {
		IPData ipData = new IPData(ipTraceResponseDTO, restCountriesResponseDTO,dist);
		ipDataRepository.save(ipData);
	}

	private IPTraceResponseDTO createDTO(String ip, IPData ipData) throws IPTraceException {
		
		logger.info("Creando y completando el DTO desde la base de datos.");

		IPTraceResponseDTO ipTraceResponseDTO = new IPTraceResponseDTO();
		
		ipTraceResponseDTO.setIp(ip);
		ipTraceResponseDTO.setCountry(ipData.getCountry());
		
		String dist = ipData.getEstimatedDistance().toString() + " kms";
		ipTraceResponseDTO.setEstimated_distance(dist);
		
		ipTraceResponseDTO.setIso_code(ipData.getIsoCode());
		
		List<String> languages = ipData.getLanguages().stream().map(l -> l.toString()).collect(Collectors.toList());
		ipTraceResponseDTO.setLanguages(languages);
		
		//Se obtiene la fecha/hora actual en formato UTC
		String formattedDate = ipTraceUtils.getFormattedDate();
		ipTraceResponseDTO.setDate(formattedDate);
		
		//Se obtiene mediante la API ExchangeRate la moneda y cotizacion respecto al Euro 
		String currency = currencyConversionService.getCurrency(ipData.getCurrency());
		ipTraceResponseDTO.setCurrency(currency);
		
		//Se aplican los diferentes husos horarios a la hora actual
		List<String> timeZones = ipData.getTimes().stream().map(t-> t.toString()).collect(Collectors.toList());
		List<String> times = ipTraceUtils.getTimes(timeZones);
		ipTraceResponseDTO.setTimes(times);
		
		return ipTraceResponseDTO;
	}

	/**
	 * @param String ip
	 * @param IP2CountryResponseDTO ip2CountryResponseDTO
	 * @param RestCountriesResponseDTO restCountriesResponseDTO
	 * @param long dist
	 * @return IPTraceResponseDTO
	 * @throws IPTraceException
	 */
	private IPTraceResponseDTO createDTO(String ip, IP2CountryResponseDTO ip2CountryResponseDTO,
			RestCountriesResponseDTO restCountriesResponseDTO, long dist) throws IPTraceException {
		
		logger.info("Creando y completando el DTO.");

		//Se obtiene la fecha/hora actual en formato UTC
		String formattedDate = ipTraceUtils.getFormattedDate();
		
		IPTraceResponseDTO ipTraceResponseDTO = new IPTraceResponseDTO();
		
		ipTraceResponseDTO.setDate(formattedDate);
		ipTraceResponseDTO.setIp(ip);
		ipTraceResponseDTO.setIso_code(ip2CountryResponseDTO.getCountryCode());
		ipTraceResponseDTO.setCountry(ip2CountryResponseDTO.getCountryName());
		
		//Se aplican los diferentes husos horarios a la hora actual
		List<String> times = ipTraceUtils.getTimes(restCountriesResponseDTO.getTimezones());
		ipTraceResponseDTO.setTimes(times);
		
		List<String> lenguages = restCountriesResponseDTO.getLanguages().stream().map(l -> l.toString()).collect(Collectors.toList());
		ipTraceResponseDTO.setLanguages(lenguages);
		
		//Se obtiene mediante la API ExchangeRate la moneda y cotizacion respecto al Euro 
		String curr = restCountriesResponseDTO.getCurrencies().stream().map(c -> c.toString()).collect(Collectors.toList()).get(0);
		String currency = currencyConversionService.getCurrency(curr);
		ipTraceResponseDTO.setCurrency(currency);
		
		ipTraceResponseDTO.setEstimated_distance(dist + " kms");
		
		return ipTraceResponseDTO;
	}
	
	/**
	 * @return StatsResponseDTO
	 * Se recupera la distancia minima y maxima a Buenos Aires 
	 * junto con el promedio de distancias e invocaciones.
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public StatsResponseDTO getStats() {
		Object[] res = (Object[]) ipDataRepository.getStats();
		Long maxDistance = (Long) res[0] == null ? 0 : (Long) res[0];
		Long minDistance = (Long) res[1] == null ? 0 : (Long) res[1];
		Long avgDistance = (Long) res[2] == null ? 0 : (Long) res[2];
		StatsResponseDTO statsResponseDTO = new StatsResponseDTO(maxDistance,minDistance,avgDistance);
		return statsResponseDTO;
	}


	public IPTraceUtils getIpTraceUtils() {
		return ipTraceUtils;
	}


	public void setIpTraceUtils(IPTraceUtils ipTraceUtils) {
		this.ipTraceUtils = ipTraceUtils;
	}


	public IPDataRepository getIpDataRepository() {
		return ipDataRepository;
	}


	public void setIpDataRepository(IPDataRepository ipDataRepository) {
		this.ipDataRepository = ipDataRepository;
	}


	public IP2CountryService getIp2CountryService() {
		return ip2CountryService;
	}


	public void setIp2CountryService(IP2CountryService ip2CountryService) {
		this.ip2CountryService = ip2CountryService;
	}


	public RestCountriesService getRestCountriesService() {
		return restCountriesService;
	}


	public void setRestCountriesService(RestCountriesService restCountriesService) {
		this.restCountriesService = restCountriesService;
	}


	public CurrencyConversionService getCurrencyConversionService() {
		return currencyConversionService;
	}


	public void setCurrencyConversionService(CurrencyConversionService currencyConversionService) {
		this.currencyConversionService = currencyConversionService;
	}
		

}
