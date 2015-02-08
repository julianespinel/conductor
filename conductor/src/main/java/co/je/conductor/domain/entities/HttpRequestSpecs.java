package co.je.conductor.domain.entities;

import java.util.Map;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class HttpRequestSpecs {
	
	@NotEmpty
	private final String httpMethod;
	
	@NotEmpty
	private final String url;
	
	private final Map<String, String> httpHeaders;
	private final JsonNode httpPayload;
	
	@JsonCreator
	public HttpRequestSpecs(@JsonProperty("httpMethod") String httpMethod, @JsonProperty("url") String url, 
	        @JsonProperty("httpHeaders") Map<String, String> httpHeaders, 
	        @JsonProperty("httpPayload") JsonNode httpPayload) {
		
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

	public JsonNode getHttpPayload() {
		return httpPayload;
	}
}