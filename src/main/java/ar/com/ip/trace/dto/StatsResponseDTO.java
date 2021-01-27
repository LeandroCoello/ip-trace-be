package ar.com.ip.trace.dto;

public class StatsResponseDTO {

	private Long maxDistance;
	private Long minDistance;
	private Long avgDistance;
	
	
	public StatsResponseDTO() {
		super();
	}
	
	public StatsResponseDTO(Long maxDistance, Long minDistance, Long avgDistance) {
		super();
		this.maxDistance = maxDistance;
		this.minDistance = minDistance;
		this.avgDistance = avgDistance;
	}
	public Long getMaxDistance() {
		return maxDistance;
	}
	public void setMaxDistance(Long maxDistance) {
		this.maxDistance = maxDistance;
	}
	public Long getMinDistance() {
		return minDistance;
	}
	public void setMinDistance(Long minDistance) {
		this.minDistance = minDistance;
	}
	public Long getAvgDistance() {
		return avgDistance;
	}
	public void setAvgDistance(Long avgDistance) {
		this.avgDistance = avgDistance;
	}
	
	
	
}
