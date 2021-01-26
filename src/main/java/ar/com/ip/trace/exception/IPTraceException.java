package ar.com.ip.trace.exception;

import org.springframework.http.HttpStatus;

public class IPTraceException extends Exception {

	private static final long serialVersionUID = -5545032666753336297L;
	private HttpStatus status;

	public IPTraceException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	
	
}
