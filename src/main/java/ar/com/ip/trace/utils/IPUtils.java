package ar.com.ip.trace.utils;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IPUtils {
	
	@Value("${ip.trace.bs.as.lat.long}")
	private List<Double> bsAsLatLong;

	public Boolean isValidIPFormat(String ip) {
		if(StringUtils.isBlank(ip)) {
			return false;
		}
		return ip.matches("([01]?[0-9]{1,2}|2[0-4][0-9]|25[0-5])");
	}
	
	public long getEstimatedDistance(List<Double> latLong) {
		Double lat1 = bsAsLatLong.get(0);
		Double lon1 = bsAsLatLong.get(1);
		
		Double lat2 = latLong.get(0);
		Double lon2 = latLong.get(1);
		
		double theta = lon1 - lon2;
		double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
		dist = Math.acos(dist);
		dist = Math.toDegrees(dist);
		dist = dist * 60 * 1.1515;
		dist = dist * 1.609344;
		
		long res = Math.round(dist);
		
		return res;
	}
}
