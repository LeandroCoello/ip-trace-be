package ar.com.ip.trace.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * @author Leandro Coello (leandro.n.coello@gmail.com)
 *
 */
@Entity
@Table(name = "ip_data_timezone")
public class IPTraceTimeZone implements Serializable {
	
	@Id
	@Column(name="ip_data_timezone_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="ip_data_timezone_descr")
	private String descr;
	
	public IPTraceTimeZone() {
		super();
	}

	public IPTraceTimeZone(String t) {
		this.descr = t;
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

	public void setDescr(String descr) {
		this.descr = descr;
	}

}
