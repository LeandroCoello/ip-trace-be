package ar.com.ip.trace.service;

import ar.com.ip.trace.exception.IPTraceException;
/**
 * @author Leandro Coello (leandro.n.coello@gmail.com)
 *
 */
public interface CurrencyConversionService {

	String getCurrency(String currency) throws IPTraceException;


}
