package ar.com.ip.trace.service;

import ar.com.ip.trace.dto.ip2country.IP2CountryResponseDTO;
import ar.com.ip.trace.exception.IPTraceException;
/**
 * @author Leandro Coello (leandro.n.coello@gmail.com)
 *
 */
public interface IP2CountryService {

	IP2CountryResponseDTO getCountryByIP(String ip) throws IPTraceException;

}
