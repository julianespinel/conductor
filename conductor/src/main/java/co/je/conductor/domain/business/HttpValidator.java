package co.je.conductor.domain.business;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.http.HttpMethod;

import com.fasterxml.jackson.databind.JsonNode;

public class HttpValidator {

	public static boolean isPostOrPutRequest(String httpMethod) {

	    boolean isPostOrPut = false;
	    
	    if (!StringUtils.isBlank(httpMethod)) {
	        
	        isPostOrPut = httpMethod.equalsIgnoreCase(HttpMethod.POST.name()) || httpMethod.equalsIgnoreCase(HttpMethod.PUT.name());
	    }
	    
		return isPostOrPut;
	}

	public static boolean requestHasPayload(JsonNode httpPayload) {
	    
	    return (httpPayload != null) && (!httpPayload.isNull());
	}
}