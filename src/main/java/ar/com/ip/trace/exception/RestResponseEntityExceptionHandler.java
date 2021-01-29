package ar.com.ip.trace.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import ar.com.ip.trace.dto.ErrorResponseDTO;

/**
 * @author Leandro Coello (leandro.n.coello@gmail.com)
 *
 * Handler para el manejo de excepciones
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * @param IPTraceException ex
     * @param WebRequest request
     * @return ResponseEntity<Object>
     */
    @ExceptionHandler(value = { IPTraceException.class }) 
    protected ResponseEntity<Object> handleTransaccionException(IPTraceException ex, WebRequest request) {
        
		Object resBody = new ErrorResponseDTO(ex.getStatus().value(), ex.getMessage());
		logger.error(ex.getMessage());
    	ex.printStackTrace();

    	return handleExceptionInternal(ex, resBody, new HttpHeaders(), ex.getStatus(), request);
    }
    /**
     * @param HttpClientErrorException ex
     * @param WebRequest request
     * @return ResponseEntity<Object>
     */
    @ExceptionHandler(value = { HttpClientErrorException.class }) 
    protected ResponseEntity<Object> handleHttpClientErrorException(HttpClientErrorException ex, WebRequest request) {
    	
    	Object resBody = new ErrorResponseDTO(HttpStatus.SERVICE_UNAVAILABLE.value(), HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase());
    	logger.error(ex.getMessage());
    	ex.printStackTrace();
    	
    	return handleExceptionInternal(ex, resBody, new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE, request);
    }

    /**
     * @param Exception ex
     * @param WebRequest request
     * @return ResponseEntity<Object>
     */
    @ExceptionHandler(value = { Exception.class }) 
    protected ResponseEntity<Object> handleInternalException(Exception ex, WebRequest request) {
        
		Object resBody = new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		logger.error(ex.getMessage());
    	ex.printStackTrace();
		
    	return handleExceptionInternal(ex, resBody, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}

