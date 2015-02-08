package co.je.conductor.domain.entities;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ConcurrencySpecs {
	
	@NotEmpty
	private final int totalCalls;
	
	@NotEmpty
	private final int concurrentCalls;
	
	@JsonCreator
	public ConcurrencySpecs(@JsonProperty("totalCalls") int totalCalls, 
	        @JsonProperty("concurrentCalls") int concurrentCalls) {
		
		this.totalCalls = totalCalls;
		this.concurrentCalls = concurrentCalls;
	}

	public int getTotalCalls() {
		return totalCalls;
	}

	public int getConcurrentCalls() {
		return concurrentCalls;
	}
}