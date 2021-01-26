package ar.com.ip.trace.entities;


public class ExtranetUsuarioCodigosPrestador {

	private String filial;
	
	private String codigoPrestador;
	
	private String  userId;

	public String getFilial() {
		if(filial.length() == 1) {
			filial = "0" + filial;
		}
		
		return filial;
	}

	public void setFilial(String filial) {
		this.filial = filial;
	}

	public String getCodigoPrestador() {
		return codigoPrestador;
	}

	public void setCodigoPrestador(String codigoPrestador) {
		this.codigoPrestador = codigoPrestador;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}