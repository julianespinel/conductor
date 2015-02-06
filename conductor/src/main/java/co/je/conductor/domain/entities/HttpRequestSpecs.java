package co.je.conductor.domain.entities;

import java.util.Map;

public class HttpRequestSpecs {
	
	private final String httpMethod;
	private final String url;
	private final Map<String, String> httpHeaders;
	private final String httpPayload;
	
	public HttpRequestSpecs(String httpMethod, String url, Map<String, String> httpHeaders, String httpPayload) {
		
		this.httpMethod = httpMethod;
		this.url = url;
		this.httpHeaders = httpHeaders;
		this.httpPayload = httpPayload;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public String getUrl() {
		return url;
	}

	public Map<String, String> getHttpHeaders() {
		return httpHeaders;
	}

	public String getHttpPayload() {
		return httpPayload;
	}
}