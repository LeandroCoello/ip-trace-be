package ar.com.ip.trace.dto.restCountries;

import java.util.List;
/**
 * @author Leandro Coello (leandro.n.coello@gmail.com)
 * DTO del servicio RestCountries
 */
public class RestCountriesResponseDTO {

	private List<Double> latlng;
	private List<String> timezones;
	private List<CurrencyDTO> currencies;
	private List<LenguageDTO> languages;

	public List<String> getTimezones() {
		return timezones;
	}
	public void setTimezones(List<String> timezones) {
		this.timezones = timezones;
	}
	public List<CurrencyDTO> getCurrencies() {
		return currencies;
	}
	public void setCurrencies(List<CurrencyDTO> currencies) {
		this.currencies = currencies;
	}
	public List<Double> getLatlng() {
		return latlng;
	}
	public void setLatlng(List<Double> latlong) {
		this.latlng = latlong;
	}
	public List<LenguageDTO> getLanguages() {
		return languages;
	}
	public void setLanguages(List<LenguageDTO> languages) {
		this.languages = languages;
	}
	
	
}
