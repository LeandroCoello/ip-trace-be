package ar.com.ip.trace.entities;


import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ar.com.ip.trace.dto.IPTraceResponseDTO;
import ar.com.ip.trace.dto.restCountries.RestCountriesResponseDTO;
/**
 * @author Leandro Coello (leandro.n.coello@gmail.com)
 *
 */
@Entity
@Table(name = "ip_data")
public class IPData implements Serializable{

	@Id
	@Column(name="ip_data_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="country")
	private String country;
		
	@Column(name="iso_code")
	private String isoCode;
	
	@Column(name="currency")
	private String currency;
		
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="ip_data_id",nullable = false)
	private List<IPTraceLanguage> languages;
	
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="ip_data_id",nullable = false)
    private List<IPTraceTimeZone> times;
	
	@Column(name="estimated_distance")
	private Long estimatedDistance;

	@Column(name="invocations")
	private Integer invocations;


	public IPData() {
		super();
	}
	public IPData(IPTraceResponseDTO ipTraceResponseDTO, RestCountriesResponseDTO restCountriesResponseDTO, long dist) {
		this.country = ipTraceResponseDTO.getCountry();
		this.isoCode = ipTraceResponseDTO.getIso_code();
		this.languages = ipTraceResponseDTO.getLanguages().stream().map(l -> new IPTraceLanguage(l)).collect(Collectors.toList());
		this.times = restCountriesResponseDTO.getTimezones().stream().map(t -> new IPTraceTimeZone(t)).collect(Collectors.toList());
		this.estimatedDistance = dist;
		this.currency = restCountriesResponseDTO.getCurrencies().get(0).getCode();
		this.invocations = 1;
	}
	
	public void updateInvocations() {
		invocations++;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getIsoCode() {
		return isoCode;
	}

	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}

	public List<IPTraceLanguage> getLanguages() {
		return languages;
	}

	public void setLenguages(List<IPTraceLanguage> languages) {
		this.languages = languages;
	}

	public List<IPTraceTimeZone> getTimes() {
		return times;
	}

	public void setTimes(List<IPTraceTimeZone> times) {
		this.times = times;
	}

	public Long getEstimatedDistance() {
		return estimatedDistance;
	}

	public void setEstimatedDistance(Long estimatedDistance) {
		this.estimatedDistance = estimatedDistance;
	}
	public Integer getInvocations() {
		return invocations;
	}
	public void setInvocations(Integer invocations) {
		this.invocations = invocations;
	}
	public void setLanguages(List<IPTraceLanguage> languages) {
		this.languages = languages;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}


}
