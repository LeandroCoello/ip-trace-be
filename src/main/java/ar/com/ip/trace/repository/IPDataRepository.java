package ar.com.ip.trace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ar.com.ip.trace.entities.IPData;
/**
 * @author Leandro Coello (leandro.n.coello@gmail.com)
 *
 */
public interface IPDataRepository extends JpaRepository<IPData, Integer>{

	IPData findByIsoCode(String countryCode);

	@Query("Select max(ipd.estimatedDistance) as maxDistance, min(ipd.estimatedDistance) as minDistance,"
			+ "sum(ipd.estimatedDistance * ipd.invocations) / sum(invocations) as avgDistance  from IPData ipd")
	Object getStats();

}
