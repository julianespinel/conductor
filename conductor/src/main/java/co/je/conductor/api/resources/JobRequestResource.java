package co.je.conductor.api.resources;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import co.je.conductor.domain.business.JobBusiness;
import co.je.conductor.domain.entities.JobRequest;
import co.je.conductor.domain.exceptions.UnsupportedJsonNodeException;
import co.je.conductor.infrastructure.utils.json.JSONUtils;

@Path("/jobs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JobRequestResource {

	private final JobBusiness jobBusiness;

	public JobRequestResource(JobBusiness jobBusiness) {

		this.jobBusiness = jobBusiness;
	}

	@POST
	public Response createJobRequest(JobRequest jobRequest) {

		Response response = null;

		try {

			String createdJobRequestId = jobBusiness.createJobRequest(jobRequest);
			Map<String, Object> json = JSONUtils.createKeyValueStringJson("jobId", createdJobRequestId);
			response = Response.status(Status.CREATED).entity(json).build();

		} catch (UnsupportedJsonNodeException e) {

			response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
		}

		return response;
	}
}