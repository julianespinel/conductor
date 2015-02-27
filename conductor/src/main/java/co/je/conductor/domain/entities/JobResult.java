package co.je.conductor.domain.entities;

import java.util.List;

import org.apache.http.HttpResponse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JobResult {

	private final String id;
	private final String jobRequestId;
	private final List<HttpResponse> httpResponsesList;

	@JsonCreator
	public JobResult(@JsonProperty("id") String id, @JsonProperty("jobRequestId") String jobRequestId, 
			@JsonProperty("httpResponsesList") List<HttpResponse> httpResponsesList) {

		this.id = id;
		this.jobRequestId = jobRequestId;
		this.httpResponsesList = httpResponsesList;
	}

	public JobResult(String jobRequestId, List<HttpResponse> httpResponsesList) {

		this.id = "0";
		this.jobRequestId = jobRequestId;
		this.httpResponsesList = httpResponsesList;
	}

	public String getId() {
		return id;
	}

	public String getJobRequestId() {
		return jobRequestId;
	}

	public List<HttpResponse> getHttpResponsesList() {
		return httpResponsesList;
	}
}