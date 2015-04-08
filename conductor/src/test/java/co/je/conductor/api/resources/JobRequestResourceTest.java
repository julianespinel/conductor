package co.je.conductor.api.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import fj.data.Either;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.junit.ResourceTestRule;

import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mockito;

import co.je.conductor.domain.business.JobBusiness;
import co.je.conductor.domain.entities.JobRequest;
import co.je.conductor.domain.exceptions.UnsupportedJsonNodeException;
import co.je.conductor.infrastructure.exceptions.IException;
import co.je.conductor.infrastructure.exceptions.TechnicalException;
import co.je.conductor.utils.JobRequestFactoryForTests;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JobRequestResourceTest {

	private static final ObjectMapper objectMapper = Jackson.newObjectMapper();
	private static final JobBusiness jobBusinessMock = Mockito.mock(JobBusiness.class);
	private static final JobRequestResource jobRequestResource = new JobRequestResource(jobBusinessMock);

	@ClassRule
	public static final ResourceTestRule resources = ResourceTestRule.builder()
	.setMapper(objectMapper).addResource(jobRequestResource).build();

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
			Either<IException, String> createdJobRequestIdEither = Either.right(expectedJobId);
			Mockito.when(jobBusinessMock.createJobRequest(Mockito.any(JobRequest.class))).thenReturn(createdJobRequestIdEither);

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
	public void testCreateJobRequest_NOK_failedCreatingJobRequest() {

		String uri = "/jobs";
		JobRequest jobRequest = JobRequestFactoryForTests.getJobRequest();

		try {

			IException technicalException = new TechnicalException("The job request could not be created.");
			Either<IException, String> createdJobRequestIdEither = Either.left(technicalException);
			Mockito.when(jobBusinessMock.createJobRequest(Mockito.any(JobRequest.class))).thenReturn(createdJobRequestIdEither);

			Response response = getDefaultHttpClient(uri).post(Entity.json(jobRequest));
			assertNotNull(response);

			int statusCode = response.getStatus();
			assertEquals(500, statusCode);

			IException exception = response.readEntity(IException.class);
			assertNotNull(exception);
			assertEquals(0, technicalException.compareTo(exception));

		} catch (UnsupportedJsonNodeException e) {

			fail("Unexpected exception: " + e.getMessage());
		}
	}
}