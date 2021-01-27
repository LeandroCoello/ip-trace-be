package ar.com.ip.trace.dto;

import java.util.List;

public class IPTraceResponseDTO {

	private String ip;
	private String date;
	private String country;
	private String iso_code;
	private List<String> languages;
	private String currency;
	private List<String> times;
	private String estimated_distance;
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getIso_code() {
		return iso_code;
	}
	public void setIso_code(String iso_code) {
		this.iso_code = iso_code;
	}
	public List<String> getLanguages() {
		return languages;
	}
	public void setLanguages(List<String> languages) {
		this.languages = languages;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public List<String> getTimes() {
		return times;
	}
	public void setTimes(List<String> times) {
		this.times = times;
	}
	public String getEstimated_distance() {
		return estimated_distance;
	}
	public void setEstimated_distance(String estimated_distance) {
		this.estimated_distance = estimated_distance;
	}
	
}
