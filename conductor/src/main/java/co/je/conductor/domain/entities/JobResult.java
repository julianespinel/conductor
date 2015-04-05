package co.je.conductor.domain.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JobResult {

	private final String id;
	private final String jobRequestId;
	private final List<HTTPConductorResponse> httpConductorResponseList;

	@JsonCreator
	public JobResult(@JsonProperty("id") String id, @JsonProperty("jobRequestId") String jobRequestId, 
			@JsonProperty("httpConductorResponseList") List<HTTPConductorResponse> httpConductorResponseList) {

		this.id = id;
		this.jobRequestId = jobRequestId;
		this.httpConductorResponseList = httpConductorResponseList;
	}

	public JobResult(String jobRequestId, List<HTTPConductorResponse> httpConductorResponseList) {

		this.id = "0";
		this.jobRequestId = jobRequestId;
		this.httpConductorResponseList = httpConductorResponseList;
	}

	public String getId() {
		return id;
	}

	public String getJobRequestId() {
		return jobRequestId;
	}

	public List<HTTPConductorResponse> getHttpConductorResponseList() {
		return httpConductorResponseList;
	}
}