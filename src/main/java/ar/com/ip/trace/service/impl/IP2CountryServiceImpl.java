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

import ar.com.ip.trace.dto.ip2country.IP2CountryResponseDTO;
import ar.com.ip.trace.exception.IPTraceException;
import ar.com.ip.trace.service.IP2CountryService;

@Service
public class IP2CountryServiceImpl implements IP2CountryService{
	
	@Value("${ip.trace.api.ip2country.info}")
	private String ip2CountryEndpoint;
	
	@Autowired
	private RestTemplate restTemplate;

	public IP2CountryResponseDTO getCountryByIP(String ip) throws IPTraceException{
			
		String endpoint = ip2CountryEndpoint + "?"+ip;
	
		ParameterizedTypeReference<IP2CountryResponseDTO> responseType = new ParameterizedTypeReference<IP2CountryResponseDTO>(){};

		ResponseEntity<IP2CountryResponseDTO> response = restTemplate.exchange(endpoint, HttpMethod.GET, new HttpEntity<String>(""), responseType);

		Boolean isSuccesfull = response.getStatusCode().is2xxSuccessful();
		if((isSuccesfull && response.getBody()==null) || !isSuccesfull) {
			throw new IPTraceException("There was a problem at calling "+endpoint+". Please reintent.",HttpStatus.SERVICE_UNAVAILABLE);
		}
		
		return response.getBody();
	}

	public String getIp2CountryEndpoint() {
		return ip2CountryEndpoint;
	}

	public void setIp2CountryEndpoint(String ip2CountryEndpoint) {
		this.ip2CountryEndpoint = ip2CountryEndpoint;
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
}
