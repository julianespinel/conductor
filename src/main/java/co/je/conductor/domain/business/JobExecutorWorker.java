package co.je.conductor.domain.business;

import java.time.Instant;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.ws.rs.core.HttpHeaders;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.http.HttpMethod;

import co.je.conductor.domain.entities.HTTPConductorResponse;
import co.je.conductor.domain.entities.HttpRequestSpecs;

public class JobExecutorWorker implements Callable<HTTPConductorResponse> {

	public static final int TIMEOUT_IN_MILLISECONDS = 5000;

	public static final RequestConfig REQUEST_CONFIG = RequestConfig.custom().setSocketTimeout(TIMEOUT_IN_MILLISECONDS)
			.setConnectTimeout(TIMEOUT_IN_MILLISECONDS).setConnectionRequestTimeout(TIMEOUT_IN_MILLISECONDS).build();

	private final HttpRequestSpecs httpRequestSpecs;
	private final String payload;

	private final CloseableHttpClient httpClient;

	public JobExecutorWorker(HttpRequestSpecs httpRequestSpecs, String payload) {

		this.httpRequestSpecs = httpRequestSpecs;
		this.payload = payload;

		httpClient = HttpClients.createDefault();
	}

	private void addConnectionTimeoutsAndHttpHeaders(RequestConfig requestConfig, AbstractHttpMessage httpRequest) {

		// Add http headers to the request
		Map<String, String> httpHeaders = httpRequestSpecs.getHttpHeaders();
		Iterator<String> keysIterator = httpHeaders.keySet().iterator();
		
		if (httpHeaders.isEmpty()) {
		    
		    // Add Accept and ContentType headers as application/json by default.
		    httpRequest.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString());
		    httpRequest.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
		    
		} else {
		    
		    while (keysIterator.hasNext()) {

	            String headerKey = keysIterator.next();
	            String headerValue = httpHeaders.get(headerKey);
	            httpRequest.setHeader(headerKey, headerValue);
	        }
		}
	}

	private void addPayload(HttpEntityEnclosingRequest httpRequest) {

	    // ContentType.APPLICATION_JSON = "application/json" and "UTF-8". See source.
		StringEntity payloadEntity = new StringEntity(payload, ContentType.APPLICATION_JSON);
		httpRequest.setEntity(payloadEntity);
	}

	@Override
	public HTTPConductorResponse call() throws Exception {

		HttpResponse result = null;
		
		long initialTime = Instant.now().toEpochMilli();

		String httpMethod = httpRequestSpecs.getHttpMethod();
		String url = httpRequestSpecs.getUrl();

		HttpUriRequest httpRequest = null;

		if (httpMethod.equalsIgnoreCase(HttpMethod.GET.name())) {

			HttpGet getRequest = new HttpGet(url);
			getRequest.setConfig(REQUEST_CONFIG);
			addConnectionTimeoutsAndHttpHeaders(REQUEST_CONFIG, getRequest);

			httpRequest = getRequest;

		} else if (httpMethod.equalsIgnoreCase(HttpMethod.POST.name())) {

			HttpPost postRequest = new HttpPost(url);
			postRequest.setConfig(REQUEST_CONFIG);
			addConnectionTimeoutsAndHttpHeaders(REQUEST_CONFIG, postRequest);
			addPayload(postRequest);

			httpRequest = postRequest;

		} else if (httpMethod.equalsIgnoreCase(HttpMethod.PUT.name())) {

			HttpPut putRequest = new HttpPut(url);
			putRequest.setConfig(REQUEST_CONFIG);
			addConnectionTimeoutsAndHttpHeaders(REQUEST_CONFIG, putRequest);
			addPayload(putRequest);

			httpRequest = putRequest;

		} else if (httpMethod.equalsIgnoreCase(HttpMethod.DELETE.name())) {

			HttpDelete deleteRequest = new HttpDelete(url);
			deleteRequest.setConfig(REQUEST_CONFIG);
			addConnectionTimeoutsAndHttpHeaders(REQUEST_CONFIG, deleteRequest);

			httpRequest = deleteRequest;

		} else {

			String message = "The method: " + httpMethod + " is not currently supported by Conductor. "
					+ "We suport the following methods: GET, POST, PUT, DELETE.";

			throw new Exception(message);
		}

		CloseableHttpResponse httpResponse = httpClient.execute(httpRequest);

		try {

			HttpEntity entityResponse = httpResponse.getEntity();
			EntityUtils.consume(entityResponse);

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			httpResponse.close();
			result = httpResponse;
			httpClient.close();
		}
		
		long finalTime = Instant.now().toEpochMilli();
		long requestDurationInMilliseconds = finalTime - initialTime;
		
		return new HTTPConductorResponse(result, requestDurationInMilliseconds);
	}
}