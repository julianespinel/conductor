package co.je.conductor.domain.entities;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ExecutionSpecs implements Comparable<ExecutionSpecs> {
	
	@NotEmpty
	private final int totalCalls;
	
	@NotEmpty
	private final int parallelCalls;
	
	@JsonCreator
	public ExecutionSpecs(@JsonProperty("totalCalls") int totalCalls,
						  @JsonProperty("parallelCalls") int parallelCalls) {
		
		this.totalCalls = totalCalls;
		this.parallelCalls = parallelCalls;
	}

	public int getTotalCalls() {
		return totalCalls;
	}

	public int getParallelCalls() {
		return parallelCalls;
	}

    @Override
    public int compareTo(ExecutionSpecs executionSpecs) {
        
        int answer = 0;
        
        answer += (totalCalls == executionSpecs.getTotalCalls()) ? 0 : 1;
        answer += (parallelCalls == executionSpecs.getParallelCalls()) ? 0 : 1;
        
        return answer;
    }
}