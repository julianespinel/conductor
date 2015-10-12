package co.je.conductor.domain.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JobRequest {

	private final String id;
	private final String creatorEmail;
	private final ExecutionSpecs executionSpecs;
	private final HttpRequestSpecs httpRequestSpecs;
	private final List<String> payloadKeysToModify;

	public JobRequest(String id, String creatorEmail, ExecutionSpecs executionSpecs,
	        HttpRequestSpecs httpRequestSpecs, List<String> payloadKeysToModify) {

		this.id = id;
		this.creatorEmail = creatorEmail;
		this.executionSpecs = executionSpecs;
		this.httpRequestSpecs = httpRequestSpecs;
		this.payloadKeysToModify = payloadKeysToModify;
	}
	
	public JobRequest(String id, JobRequest jobRequest) {
	    
	    this.id = id;
	    this.creatorEmail = jobRequest.getCreatorEmail();
        this.executionSpecs = jobRequest.getExecutionSpecs();
        this.httpRequestSpecs = jobRequest.getHttpRequestSpecs();
        this.payloadKeysToModify = jobRequest.getPayloadKeysToModify();
    }

	@JsonCreator
	public JobRequest(@JsonProperty("creatorEmail") String creatorEmail, 
            @JsonProperty("executionSpecs") ExecutionSpecs executionSpecs,
            @JsonProperty("httpRequestSpecs") HttpRequestSpecs httpRequestSpecs,
            @JsonProperty("payloadKeysToModify") List<String> payloadKeysToModify) {

		this.id = "0";
		this.creatorEmail = creatorEmail;
		this.executionSpecs = executionSpecs;
		this.httpRequestSpecs = httpRequestSpecs;
		this.payloadKeysToModify = payloadKeysToModify;
	}
	
	public String getId() {
		return id;
	}

	public String getCreatorEmail() {
		return creatorEmail;
	}

	public ExecutionSpecs getExecutionSpecs() {
		return executionSpecs;
	}

	public HttpRequestSpecs getHttpRequestSpecs() {
		return httpRequestSpecs;
	}

	public List<String> getPayloadKeysToModify() {
		return payloadKeysToModify;
	}
}