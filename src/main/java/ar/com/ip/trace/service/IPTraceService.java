package ar.com.ip.trace.service;

import ar.com.ip.trace.dto.IPTraceResponseDTO;
import ar.com.ip.trace.dto.StatsResponseDTO;
import ar.com.ip.trace.exception.IPTraceException;
/**
 * @author Leandro Coello (leandro.n.coello@gmail.com)
 *
 */
public interface IPTraceService {

	public IPTraceResponseDTO getTraceData(String ip) throws IPTraceException;

	public StatsResponseDTO getStats();

}
