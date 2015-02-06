package co.je.conductor.domain.entities;

import java.util.List;

public class LoadTestRequest {
	
	private final String creatorEmail;
	private final ConcurrencySpecs concurrencySpecs;
	private final HttpRequestSpecs httpRequestSpecs;
	private final List<String> payloadKeysToIterate;
	
	public LoadTestRequest(String creatorEmail, ConcurrencySpecs concurrencySpecs, 
			HttpRequestSpecs httpRequestSpecs, List<String> payloadKeysToIterate) {
		
		this.creatorEmail = creatorEmail;
		this.concurrencySpecs = concurrencySpecs;
		this.httpRequestSpecs = httpRequestSpecs;
		this.payloadKeysToIterate = payloadKeysToIterate;
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

	public List<String> getPayloadKeysToIterate() {
		return payloadKeysToIterate;
	}
}