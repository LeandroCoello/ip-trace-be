package ar.com.ip.trace.controller.rest;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.com.ip.trace.dto.IPRequestDTO;
import ar.com.ip.trace.dto.IPTraceResponseDTO;
import ar.com.ip.trace.exception.IPTraceException;
import ar.com.ip.trace.service.IPTraceService;
import ar.com.ip.trace.utils.IPUtils;

@RestController
@RequestMapping("/hola")
@CrossOrigin(origins="*")
public class IPTraceController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    private IPTraceService ipTraceService;
	
	@Autowired
	private IPUtils ipUtils;
	
	
	@GetMapping("/ping")
	public String healthcheck(){
		return "OK";
	}

	@PostMapping(path = "/trace", consumes = "application/json", produces = "application/json")
	public ResponseEntity<IPTraceResponseDTO> traceIP(@RequestBody IPRequestDTO ipRequestDTO) throws IPTraceException{

		if(ipRequestDTO == null  || StringUtils.isBlank(ipRequestDTO.getIp()) || ipUtils.isValidIPFormat(ipRequestDTO.getIp())) {
			throw new IPTraceException("Null body or IP.", HttpStatus.BAD_REQUEST);
		}
		IPTraceResponseDTO ipTraceData = ipTraceService.getTraceData(ipRequestDTO.getIp());
		return new ResponseEntity<IPTraceResponseDTO>(ipTraceData, HttpStatus.OK);
	}
	
//	@GetMapping(path = "/stats", consumes = "application/json", produces = "application/json")
//	public ResponseEntity<TransaccionDetalleDTO> getDetalleTransaccion(@RequestHeader(required = true, value = VisualizadorTransaccionesBackendApplication.HEADER_CUSTOM_AUTH) String authorizationHeader,
//			@RequestBody IdTransaccionDTO idTransaccionDTO) throws TransaccionException {
//		logger.info("Procesando getDetalleTransacciones.");
//		TransaccionDetalleDTO transaccionDetalle = transaccionService.getDetalleTransacciones(authorizationHeader,idTransaccionDTO);
//	
//		return new ResponseEntity<TransaccionDetalleDTO>(transaccionDetalle, HttpStatus.OK);
//	}
	
	
	
}
