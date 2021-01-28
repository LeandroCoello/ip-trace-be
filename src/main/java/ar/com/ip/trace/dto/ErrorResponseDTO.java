package ar.com.ip.trace.dto;

/**
 * @author Leandro Coello (leandro.n.coello@gmail.com)
 *
 */
public class ErrorResponseDTO {

	private int status;
	private String msg;
	
	public ErrorResponseDTO(int status, String msg) {
		super();
		this.status = status;
		this.msg = msg;
	}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
