package ar.com.ip.trace.service;

import ar.com.ip.trace.dto.restCountries.RestCountriesResponseDTO;
import ar.com.ip.trace.exception.IPTraceException;
/**
 * @author Leandro Coello (leandro.n.coello@gmail.com)
 *
 */
public interface RestCountriesService {

	RestCountriesResponseDTO getCountryData(String countryCode) throws IPTraceException;


}
