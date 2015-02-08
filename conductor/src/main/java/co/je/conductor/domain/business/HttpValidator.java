package co.je.conductor.domain.business;

import org.eclipse.jetty.http.HttpMethod;

import com.fasterxml.jackson.databind.JsonNode;

public class HttpValidator {

	public static boolean isPostOrPutRequest(String httpMethod) {

		return httpMethod.equalsIgnoreCase(HttpMethod.POST.name()) || httpMethod.equalsIgnoreCase(HttpMethod.PUT.name());
	}

	public static boolean requestHasPayload(JsonNode httpPayload) {
	    
	    return !httpPayload.isNull();
	}
}