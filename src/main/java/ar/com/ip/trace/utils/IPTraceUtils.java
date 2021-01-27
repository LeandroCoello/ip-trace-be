package ar.com.ip.trace.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IPTraceUtils {
	
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
	
	public String getDateWithTimeZone(String tz) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		Calendar cal = Calendar.getInstance();
		
		String op = tz.substring(3,4);
		Integer hOffset = Integer.parseInt(tz.substring(4,6));
		Integer mOffset = Integer.parseInt(tz.substring(7,9));
		if(StringUtils.equals(op, "-")) {
			hOffset = -hOffset;
			mOffset = -mOffset;		
		}
		cal.add(Calendar.HOUR, hOffset);
		cal.add(Calendar.MINUTE, mOffset);
		String date = sdf.format(cal.getTime());
		
		return date + " (" + tz +")";
	}
	
	public String getFormattedDate() {
		
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YY HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		String date = sdf.format(now) + " (UTC)";
		
		return date;
	}
	
	public List<String> getDates(List<String> timeZones) {
		List<String> res = timeZones.stream().map(tz -> getDateWithTimeZone(tz)).collect(Collectors.toList());
		return res;
	}

}
