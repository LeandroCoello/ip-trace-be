package ar.com.ip.trace.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ar.com.ip.trace.dto.converter.ConverterApiDTO;
import ar.com.ip.trace.exception.IPTraceException;
import ar.com.ip.trace.service.CurrencyConversionService;

@Service
public class CurrencyConversionServiceImpl implements CurrencyConversionService {
	
	@Value("${ip.trace.api.fixer}")
	private String fixerApiEndpoint;	
	
	@Value("${ip.trace.fixer.api.key}")
	private String fixerApiKey;	
	
	@Autowired
	private RestTemplate restTemplate;

	public String getCurrency(String currency) throws IPTraceException {
		
		String endpoint = fixerApiEndpoint+fixerApiKey+"/pair/EUR/"+currency;
		
		ParameterizedTypeReference<ConverterApiDTO> responseType = new ParameterizedTypeReference<ConverterApiDTO>(){};

		ResponseEntity<ConverterApiDTO> response = restTemplate.exchange(endpoint, HttpMethod.GET, new HttpEntity<String>(""), responseType);

		Boolean isSuccesfull = response.getStatusCode().is2xxSuccessful();
		if((isSuccesfull && response.getBody()==null) || !isSuccesfull) {
			throw new IPTraceException("There was a problem at calling "+endpoint+". Please reintent.",HttpStatus.SERVICE_UNAVAILABLE);
		}
		Double rate = response.getBody().getConversion_rate();
		
		return currency + " (1 EUR = " + rate + " "+currency+")";
	}

	public String getFixerApiEndpoint() {
		return fixerApiEndpoint;
	}

	public void setFixerApiEndpoint(String fixerApiEndpoint) {
		this.fixerApiEndpoint = fixerApiEndpoint;
	}

	public String getFixerApiKey() {
		return fixerApiKey;
	}

	public void setFixerApiKey(String fixerApiKey) {
		this.fixerApiKey = fixerApiKey;
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
}
