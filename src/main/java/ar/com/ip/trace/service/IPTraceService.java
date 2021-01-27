package ar.com.ip.trace.service;

import ar.com.ip.trace.dto.IPTraceResponseDTO;
import ar.com.ip.trace.dto.StatsResponseDTO;
import ar.com.ip.trace.exception.IPTraceException;

public interface IPTraceService {

	public IPTraceResponseDTO getTraceData(String ip) throws IPTraceException;

	public StatsResponseDTO getStats();

}
