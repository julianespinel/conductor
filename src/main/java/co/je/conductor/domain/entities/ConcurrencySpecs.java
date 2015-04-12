package co.je.conductor.domain.entities;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ConcurrencySpecs implements Comparable<ConcurrencySpecs> {
	
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

    @Override
    public int compareTo(ConcurrencySpecs concurrencySpecs) {
        
        int answer = 0;
        
        answer += (totalCalls == concurrencySpecs.getTotalCalls()) ? 0 : 1;
        answer += (concurrentCalls == concurrencySpecs.getConcurrentCalls()) ? 0 : 1;
        
        return answer;
    }
}