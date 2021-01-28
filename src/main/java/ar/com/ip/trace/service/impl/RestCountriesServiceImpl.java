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

import ar.com.ip.trace.dto.restCountries.RestCountriesResponseDTO;
import ar.com.ip.trace.exception.IPTraceException;
import ar.com.ip.trace.service.RestCountriesService;

/**
 * @author Leandro Coello (leandro.n.coello@gmail.com)
 * 
 * Service de la API RestCountries
 */
@Service
public class RestCountriesServiceImpl implements RestCountriesService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	
	@Value("${ip.trace.api.rest.countries}")
	private String restCountriesEndpoint;	
	
	@Autowired
	private RestTemplate restTemplate;

	/**
	 * @param String code
	 * @return RestCountriesResponseDTO
	 * @throws IPTraceException
	 */
	public RestCountriesResponseDTO getCountryData(String code) throws IPTraceException {
		
		// Se arma endpoint con parametros
		String endpoint = restCountriesEndpoint + code;
		logger.info("Invocando a: "+endpoint);

		// Se define el tipo de la respuesta del servicio
		ParameterizedTypeReference<RestCountriesResponseDTO> responseType = new ParameterizedTypeReference<RestCountriesResponseDTO>(){};

		// Se realiza la invocacion
		ResponseEntity<RestCountriesResponseDTO> response = restTemplate.exchange(endpoint, HttpMethod.GET, new HttpEntity<String>(""), responseType);

		// Validacion de la respuesta
		Boolean isSuccesfull = response.getStatusCode().is2xxSuccessful();
		if((isSuccesfull && response.getBody()==null) || !isSuccesfull) {
			throw new IPTraceException("There was a problem at calling "+endpoint+". Please reintent.",HttpStatus.SERVICE_UNAVAILABLE);
		}
		
		return response.getBody();
	}

	public String getRestCountriesEndpoint() {
		return restCountriesEndpoint;
	}

	public void setRestCountriesEndpoint(String restCountriesEndpoint) {
		this.restCountriesEndpoint = restCountriesEndpoint;
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
}
