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

import ar.com.ip.trace.dto.restCountries.RestCountriesResponseDTO;
import ar.com.ip.trace.exception.IPTraceException;
import ar.com.ip.trace.service.RestCountriesService;

@Service
public class RestCountriesServiceImpl implements RestCountriesService {
	
	@Value("${ip.trace.api.rest.countries}")
	private String restCountriesEndpoint;	
	
	@Autowired
	private RestTemplate restTemplate;

	public RestCountriesResponseDTO getCountryData(String code) throws IPTraceException {
		
		String endpoint = restCountriesEndpoint + code;
		
		ParameterizedTypeReference<RestCountriesResponseDTO> responseType = new ParameterizedTypeReference<RestCountriesResponseDTO>(){};

		ResponseEntity<RestCountriesResponseDTO> response = restTemplate.exchange(endpoint, HttpMethod.GET, new HttpEntity<String>(""), responseType);

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
