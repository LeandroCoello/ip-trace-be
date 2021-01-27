package ar.com.ip.trace.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "ip_data_language")
public class IPTraceLanguage implements Serializable {

	@Id
	@Column(name="ip_data_language_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="ip_data_language_descr")
	private String descr;
	
	public IPTraceLanguage() {
		super();
	}
	public IPTraceLanguage(String l) {
		this.descr = l;
	}

	public String toString() {
		return this.descr;
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescr() {
		return descr;
	}

}
