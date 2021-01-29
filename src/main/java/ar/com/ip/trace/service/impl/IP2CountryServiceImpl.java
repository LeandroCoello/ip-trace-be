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

import ar.com.ip.trace.dto.ip2country.IP2CountryResponseDTO;
import ar.com.ip.trace.exception.IPTraceException;
import ar.com.ip.trace.service.IP2CountryService;

/**
 * @author Leandro Coello (leandro.n.coello@gmail.com)
 *
 * Service de la API IP2Country
 */
@Service
public class IP2CountryServiceImpl implements IP2CountryService{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${ip.trace.api.ip2country.info}")
	private String ip2CountryEndpoint;
	
	@Autowired
	private RestTemplate restTemplate;

	/**
	 * @param String ip
	 * @return IP2CountryResponseDTO
	 * @throws IPTraceException
	 */
	public IP2CountryResponseDTO getCountryByIP(String ip) throws IPTraceException{
		
		// Se arma endpoint con parametros
		String endpoint = ip2CountryEndpoint + "?"+ip;
		logger.info("Invocando a: "+endpoint);
	
		// Se define el tipo de la respuesta del servicio
		ParameterizedTypeReference<IP2CountryResponseDTO> responseType = new ParameterizedTypeReference<IP2CountryResponseDTO>(){};

		// Se realiza la invocacion
		ResponseEntity<IP2CountryResponseDTO> response = restTemplate.exchange(endpoint, HttpMethod.GET, new HttpEntity<String>(""), responseType);

		// Validacion de la respuesta
		Boolean isSuccesfull = response.getStatusCode().is2xxSuccessful();
		if((isSuccesfull && response.getBody()==null) || !isSuccesfull) {
			throw new IPTraceException("Hubo un inconveniente invocando a: "+endpoint+". Por favor reintente.",HttpStatus.SERVICE_UNAVAILABLE);
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
