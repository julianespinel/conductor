package co.je.conductor.api.resources;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import co.je.conductor.domain.business.JobBusiness;
import co.je.conductor.domain.entities.JobRequest;
import co.je.conductor.infrastructure.exceptions.IException;
import co.je.conductor.infrastructure.utils.json.JSONUtils;
import fj.data.Either;

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
		
		Either<IException, String> createdJobRequestIdEither = jobBusiness.createJobRequest(jobRequest);
		
		if (createdJobRequestIdEither.isRight()) {
			
			String createdJobRequestId = createdJobRequestIdEither.right().value();
			Map<String, Object> json = JSONUtils.createKeyValueStringJson("jobID", createdJobRequestId);
			response = Response.status(201).entity(json).build();
			
		} else {
			
			IException exception = createdJobRequestIdEither.left().value();
			response = Response.status(500).entity(exception).build();
		}
		
		return response;
	}
}