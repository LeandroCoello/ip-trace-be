package ar.com.ip.trace.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ar.com.ip.trace.dto.IPTraceResponseDTO;
import ar.com.ip.trace.dto.converter.ConverterApiDTO;
import ar.com.ip.trace.dto.converter.ConverterInfoDTO;
import ar.com.ip.trace.dto.ip2country.IP2CountryResponseDTO;
import ar.com.ip.trace.dto.restCountries.CurrencyDTO;
import ar.com.ip.trace.dto.restCountries.RestCountriesResponseDTO;
import ar.com.ip.trace.exception.IPTraceException;
import ar.com.ip.trace.service.IPTraceService;
import ar.com.ip.trace.utils.IPUtils;


@Service
@Transactional
public class IPTraceServiceImpl implements IPTraceService{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${ip.trace.api.ip2country.info}")
	private String ip2CountryEndpoint;	

	@Value("${ip.trace.api.rest.countries}")
	private String restCountriesEndpoint;	
	
	@Value("${ip.trace.api.fixer}")
	private String fixerApiEndpoint;	
	
	@Value("${ip.trace.fixer.api.key}")
	private String fixerApiKey;	
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private IPUtils ipUtils;
		
	
	public IPTraceResponseDTO getTraceData(String ip) throws IPTraceException {
		
		logger.info("getting data for ip: "+ip);
		
		IP2CountryResponseDTO ip2CountryResponseDTO = getCountryByIP(ip);
		
		RestCountriesResponseDTO restCountriesResponseDTO = getCountryData(ip2CountryResponseDTO.getCountryCode());
		
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YY hh:mm:ss");
		
		IPTraceResponseDTO ipTraceResponseDTO = new IPTraceResponseDTO();
		
		ipTraceResponseDTO.setDate(sdf.format(now));
		ipTraceResponseDTO.setIp(ip);
		ipTraceResponseDTO.setIso_code(ip2CountryResponseDTO.getCountryCode());
		ipTraceResponseDTO.setCountry(ip2CountryResponseDTO.getCountryName());
		
		List<String> dates = getDates(restCountriesResponseDTO.getTimezones());
		ipTraceResponseDTO.setTimes(dates);
		
		List<String> lenguages = restCountriesResponseDTO.getLanguages().stream().map(l -> l.toString()).collect(Collectors.toList());
		ipTraceResponseDTO.setLenguages(lenguages);
		
		String currency = getCurrency(restCountriesResponseDTO.getCurrencies());
		ipTraceResponseDTO.setCurrency(currency);
		
		long dist = ipUtils.getEstimatedDistance(restCountriesResponseDTO.getLatlng());
		ipTraceResponseDTO.setEstimated_distance(dist + " kms");
		
		return ipTraceResponseDTO;
	}

		
	private String getCurrency(List<CurrencyDTO> currencies) throws IPTraceException {
		String curr = currencies.stream().map(c -> c.toString()).collect(Collectors.toList()).get(0);
				
		String endpoint = fixerApiEndpoint+fixerApiKey+"/pair/EUR/"+curr;
		
		ParameterizedTypeReference<ConverterApiDTO> responseType = new ParameterizedTypeReference<ConverterApiDTO>(){};

		ResponseEntity<ConverterApiDTO> response = restTemplate.exchange(endpoint, HttpMethod.GET, new HttpEntity<String>(""), responseType);

		Boolean isSuccesfull = response.getStatusCode().is2xxSuccessful();
		if((isSuccesfull && response.getBody()==null) || !isSuccesfull) {
			throw new IPTraceException("There was a problem at calling "+endpoint+". Please reintent.",HttpStatus.SERVICE_UNAVAILABLE);
		}
		Double rate = response.getBody().getConversion_rate();
		
		return curr + " (1 EUR = " + rate + " "+curr+")";
	}


	private List<String> getDates(List<String> timeZones) {
		List<String> res = timeZones.stream().map(tz -> getDateWithTimeZone(tz)).collect(Collectors.toList());
		return res;
	}
	
	private String getDateWithTimeZone(String tz) {
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		Calendar cal = Calendar.getInstance();
		
		String op = tz.substring(3,4);
		Integer hOffset = Integer.parseInt(tz.substring(4,6));
		Integer mOffset = Integer.parseInt(tz.substring(7,9));
		if(StringUtils.equals(op, "-")) {
			hOffset = -hOffset;
			mOffset = -mOffset;		
		}
		cal.add(Calendar.HOUR, hOffset);
		cal.add(Calendar.MINUTE, mOffset);
		String date = sdf.format(cal.getTime());
		
		return date + " (" + tz +")";
	}

	private IP2CountryResponseDTO getCountryByIP(String ip) throws IPTraceException{
		
		String endpoint = ip2CountryEndpoint + "?"+ip;
	
		ParameterizedTypeReference<IP2CountryResponseDTO> responseType = new ParameterizedTypeReference<IP2CountryResponseDTO>(){};

		ResponseEntity<IP2CountryResponseDTO> response = restTemplate.exchange(endpoint, HttpMethod.GET, new HttpEntity<String>(""), responseType);

		Boolean isSuccesfull = response.getStatusCode().is2xxSuccessful();
		if((isSuccesfull && response.getBody()==null) || !isSuccesfull) {
			throw new IPTraceException("There was a problem at calling "+endpoint+". Please reintent.",HttpStatus.SERVICE_UNAVAILABLE);
		}
		
		return response.getBody();
	}
	
	private RestCountriesResponseDTO getCountryData(String code) throws IPTraceException {
		
		String endpoint = restCountriesEndpoint + code;
		
		ParameterizedTypeReference<RestCountriesResponseDTO> responseType = new ParameterizedTypeReference<RestCountriesResponseDTO>(){};

		ResponseEntity<RestCountriesResponseDTO> response = restTemplate.exchange(endpoint, HttpMethod.GET, new HttpEntity<String>(""), responseType);

		Boolean isSuccesfull = response.getStatusCode().is2xxSuccessful();
		if((isSuccesfull && response.getBody()==null) || !isSuccesfull) {
			throw new IPTraceException("There was a problem at calling "+endpoint+". Please reintent.",HttpStatus.SERVICE_UNAVAILABLE);
		}
		
		return response.getBody();
	}

}
