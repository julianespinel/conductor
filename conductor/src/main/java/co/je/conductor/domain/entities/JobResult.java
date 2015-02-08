package co.je.conductor.domain.entities;

import java.util.List;

import org.apache.http.HttpResponse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JobResult {

	private final String id;
	private final String jobRequestID;
	private final List<HttpResponse> httpResponsesList;

	@JsonCreator
	public JobResult(@JsonProperty("id") String id, @JsonProperty("jobRequestID") String jobRequestID, 
			@JsonProperty("httpResponsesList") List<HttpResponse> httpResponsesList) {

		this.id = id;
		this.jobRequestID = jobRequestID;
		this.httpResponsesList = httpResponsesList;
	}

	public JobResult(String jobRequestID, List<HttpResponse> httpResponsesList) {

		this.id = "0";
		this.jobRequestID = jobRequestID;
		this.httpResponsesList = httpResponsesList;
	}

	public String getId() {
		return id;
	}

	public String getJobRequestID() {
		return jobRequestID;
	}

	public List<HttpResponse> getHttpResponsesList() {
		return httpResponsesList;
	}
}