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
		
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public IPTraceResponseDTO getTraceData(String ip) throws IPTraceException {
		
		logger.info("getting data for ip: "+ip);
		IPTraceResponseDTO ipTraceResponseDTO;
		IP2CountryResponseDTO ip2CountryResponseDTO = ip2CountryService.getCountryByIP(ip);
		
		logger.info("getting data from db");
		IPData ipData = ipDataRepository.findAllByIsoCode(ip2CountryResponseDTO.getCountryCode());
		
		if(ipData != null) {
			ipTraceResponseDTO = createDTO(ip, ipData, ip2CountryResponseDTO);
			ipData.updateInvocations();
			saveData(ipData);
			return ipTraceResponseDTO;
		}
		
		RestCountriesResponseDTO restCountriesResponseDTO = restCountriesService.getCountryData(ip2CountryResponseDTO.getCountryCode());
		
		ipTraceResponseDTO = createDTO(ip, ip2CountryResponseDTO, restCountriesResponseDTO);
		
		saveData(ipTraceResponseDTO,restCountriesResponseDTO);
				
		return ipTraceResponseDTO;
	}


	private void saveData(IPData ipData) {
		ipDataRepository.save(ipData);
	}

	private void saveData(IPTraceResponseDTO ipTraceResponseDTO, RestCountriesResponseDTO restCountriesResponseDTO) {
		long dist = ipTraceUtils.getEstimatedDistance(restCountriesResponseDTO.getLatlng());
		ipTraceResponseDTO.setEstimated_distance(dist + " kms");

		IPData ipData = new IPData(ipTraceResponseDTO, restCountriesResponseDTO,dist);
		ipDataRepository.save(ipData);
	}

	private IPTraceResponseDTO createDTO(String ip, IPData ipData, IP2CountryResponseDTO ip2CountryResponseDTO) throws IPTraceException {
		IPTraceResponseDTO ipTraceResponseDTO = new IPTraceResponseDTO();
		
		ipTraceResponseDTO.setIp(ip);
		ipTraceResponseDTO.setCountry(ipData.getCountry());
		
		String dist = ipData.getEstimatedDistance().toString() + " kms";
		ipTraceResponseDTO.setEstimated_distance(dist);
		ipTraceResponseDTO.setIso_code(ipData.getIsoCode());
		
		List<String> languages = ipData.getLanguages().stream().map(l -> l.toString()).collect(Collectors.toList());
		ipTraceResponseDTO.setLanguages(languages);
		
		String formattedDate = ipTraceUtils.getFormattedDate();
		ipTraceResponseDTO.setDate(formattedDate);
		
		String currency = currencyConversionService.getCurrency(ipData.getCurrency());
		ipTraceResponseDTO.setCurrency(currency);
		
		List<String> timeZones = ipData.getTimes().stream().map(t-> t.toString()).collect(Collectors.toList());
		List<String> times = ipTraceUtils.getDates(timeZones);
		ipTraceResponseDTO.setTimes(times);
		
		return ipTraceResponseDTO;
	}

	private IPTraceResponseDTO createDTO(String ip, IP2CountryResponseDTO ip2CountryResponseDTO,
			RestCountriesResponseDTO restCountriesResponseDTO) throws IPTraceException {
		
		String formattedDate = ipTraceUtils.getFormattedDate();
		
		IPTraceResponseDTO ipTraceResponseDTO = new IPTraceResponseDTO();
		
		ipTraceResponseDTO.setDate(formattedDate);
		ipTraceResponseDTO.setIp(ip);
		ipTraceResponseDTO.setIso_code(ip2CountryResponseDTO.getCountryCode());
		ipTraceResponseDTO.setCountry(ip2CountryResponseDTO.getCountryName());
		
		List<String> dates = ipTraceUtils.getDates(restCountriesResponseDTO.getTimezones());
		ipTraceResponseDTO.setTimes(dates);
		
		List<String> lenguages = restCountriesResponseDTO.getLanguages().stream().map(l -> l.toString()).collect(Collectors.toList());
		ipTraceResponseDTO.setLanguages(lenguages);
		
		String curr = restCountriesResponseDTO.getCurrencies().stream().map(c -> c.toString()).collect(Collectors.toList()).get(0);
		String currency = currencyConversionService.getCurrency(curr);
		ipTraceResponseDTO.setCurrency(currency);
		
		long dist = ipTraceUtils.getEstimatedDistance(restCountriesResponseDTO.getLatlng());
		ipTraceResponseDTO.setEstimated_distance(dist + " kms");
		
		return ipTraceResponseDTO;
	}
	
	public StatsResponseDTO getStats() {
		Object[] res = (Object[]) ipDataRepository.getStats();
		Long maxDistance = (Long) res[0];
		Long minDistance = (Long) res[1];
		Long avgDistance = (Long) res[2];
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
