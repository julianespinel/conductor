package co.je.conductor.domain.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JobRequest {

	private final String id;
	private final String creatorEmail;
	private final ConcurrencySpecs concurrencySpecs;
	private final HttpRequestSpecs httpRequestSpecs;
	private final List<String> payloadKeysToModify;

	public JobRequest(String id, String creatorEmail, ConcurrencySpecs concurrencySpecs, 
	        HttpRequestSpecs httpRequestSpecs, List<String> payloadKeysToModify) {

		this.id = id;
		this.creatorEmail = creatorEmail;
		this.concurrencySpecs = concurrencySpecs;
		this.httpRequestSpecs = httpRequestSpecs;
		this.payloadKeysToModify = payloadKeysToModify;
	}
	
	public JobRequest(String id, JobRequest jobRequest) {
	    
	    this.id = id;
	    this.creatorEmail = jobRequest.getCreatorEmail();
        this.concurrencySpecs = jobRequest.getConcurrencySpecs();
        this.httpRequestSpecs = jobRequest.getHttpRequestSpecs();
        this.payloadKeysToModify = jobRequest.getPayloadKeysToModify();
    }

	@JsonCreator
	public JobRequest(@JsonProperty("creatorEmail") String creatorEmail, 
            @JsonProperty("concurrencySpecs") ConcurrencySpecs concurrencySpecs, 
            @JsonProperty("httpRequestSpecs") HttpRequestSpecs httpRequestSpecs,
            @JsonProperty("payloadKeysToModify") List<String> payloadKeysToModify) {

		this.id = "0";
		this.creatorEmail = creatorEmail;
		this.concurrencySpecs = concurrencySpecs;
		this.httpRequestSpecs = httpRequestSpecs;
		this.payloadKeysToModify = payloadKeysToModify;
	}
	
	public String getId() {
		return id;
	}

	public String getCreatorEmail() {
		return creatorEmail;
	}

	public ConcurrencySpecs getConcurrencySpecs() {
		return concurrencySpecs;
	}

	public HttpRequestSpecs getHttpRequestSpecs() {
		return httpRequestSpecs;
	}

	public List<String> getPayloadKeysToModify() {
		return payloadKeysToModify;
	}
}