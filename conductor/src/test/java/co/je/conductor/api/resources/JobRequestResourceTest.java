package co.je.conductor.api.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.junit.ResourceTestRule;

import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import co.je.conductor.domain.business.JobBusiness;
import co.je.conductor.domain.entities.JobRequest;
import co.je.conductor.domain.exceptions.UnsupportedJsonNodeException;
import co.je.conductor.utils.JobRequestFactoryForTests;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JobRequestResourceTest {

	private final ObjectMapper objectMapper = Jackson.newObjectMapper();
	private final JobBusiness jobBusinessMock = Mockito.mock(JobBusiness.class);
	private final JobRequestResource jobRequestResource = new JobRequestResource(jobBusinessMock);

	@Rule
	public final ResourceTestRule resources = ResourceTestRule.builder().setMapper(objectMapper).addResource(jobRequestResource).build();

	private Builder getDefaultHttpClient(String uri) {

		return resources.client().target(uri)
				.request(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE);
	}

	@Test
	public void testCreateJobRequest_OK() {

		String uri = "/jobs";
		JobRequest jobRequest = JobRequestFactoryForTests.getJobRequest();

		try {

			String expectedJobId = "234";
			Mockito.when(jobBusinessMock.createJobRequest(Mockito.any(JobRequest.class))).thenReturn(expectedJobId);

			Response response = getDefaultHttpClient(uri).post(Entity.json(jobRequest));
			assertNotNull(response);

			int statusCode = response.getStatus();
			assertEquals(201, statusCode);

			Map<String, Object> mapResponse = response.readEntity(Map.class);
			assertNotNull(mapResponse);
			assertEquals(expectedJobId, mapResponse.get("jobId"));

		} catch (UnsupportedJsonNodeException e) {

			fail("Unexpected exception: " + e.getMessage());
		}
	}	
	
	@Test
	public void testCreateJobRequest_NOK_unsupportedJsonNodeException() {

		String uri = "/jobs";
		JobRequest jobRequest = JobRequestFactoryForTests.getJobRequest();

		try {

			Mockito.doThrow(UnsupportedJsonNodeException.class).when(jobBusinessMock).createJobRequest(Mockito.any(JobRequest.class));
			Response response = getDefaultHttpClient(uri).post(Entity.json(jobRequest));
			assertNotNull(response);

			int statusCode = response.getStatus();
			assertEquals(500, statusCode);

			UnsupportedJsonNodeException exception = response.readEntity(UnsupportedJsonNodeException.class);
			assertNotNull(exception);

		} catch (UnsupportedJsonNodeException e) {

			fail("Unexpected exception: " + e.getMessage());
		}
	}
}