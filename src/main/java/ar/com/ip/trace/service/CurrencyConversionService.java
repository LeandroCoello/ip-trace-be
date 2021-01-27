package ar.com.ip.trace.service;

import ar.com.ip.trace.exception.IPTraceException;

public interface CurrencyConversionService {

	String getCurrency(String currency) throws IPTraceException;


}
