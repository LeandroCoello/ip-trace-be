package ar.com.ip.trace.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * @author Leandro Coello (leandro.n.coello@gmail.com)
 * 
 * Service de la API ExchangeRate
 */
@Service
public class CurrencyConversionServiceImpl implements CurrencyConversionService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	
	@Value("${ip.trace.api.fixer}")
	private String fixerApiEndpoint;	
	
	@Value("${ip.trace.fixer.api.key}")
	private String fixerApiKey;	
	
	@Autowired
	private RestTemplate restTemplate;

	/**
	 * @param String currency
	 * @return String
	 * @throws IPTraceException
	 */
	public String getCurrency(String currency) throws IPTraceException {
		
		// Se arma endpoint con parametros
		String endpoint = fixerApiEndpoint+fixerApiKey+"/pair/EUR/"+currency;
		logger.info("Invocando a: "+endpoint);

		// Se define el tipo de la respuesta del servicio
		ParameterizedTypeReference<ConverterApiDTO> responseType = new ParameterizedTypeReference<ConverterApiDTO>(){};

		// Se realiza la invocacion
		ResponseEntity<ConverterApiDTO> response = restTemplate.exchange(endpoint, HttpMethod.GET, new HttpEntity<String>(""), responseType);

		// Validacion de la respuesta
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
