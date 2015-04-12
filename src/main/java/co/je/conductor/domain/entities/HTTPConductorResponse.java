package co.je.conductor.domain.entities;

import org.apache.http.HttpResponse;

public class HTTPConductorResponse {
	
	private final HttpResponse httpResponse;
	private final long responseTimeInMilliseconds;
	
	public HTTPConductorResponse(HttpResponse httpResponse, long responseTimeInMilliseconds) {
		
		this.httpResponse = httpResponse;
		this.responseTimeInMilliseconds = responseTimeInMilliseconds;
	}

	public HttpResponse getHttpResponse() {
		return httpResponse;
	}

	public long getResponseTimeInMilliseconds() {
		return responseTimeInMilliseconds;
	}
}