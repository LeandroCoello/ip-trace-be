package ar.com.ip.trace.dto.ip2country;

/**
 * @author Leandro Coello (leandro.n.coello@gmail.com)
 * DTO del servicio IP2Country
 */
public class IP2CountryResponseDTO {

	private String countryCode;
	private String countryName;
	
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
	
}
