package co.je.conductor.api.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import fj.data.Either;
import io.dropwizard.testing.junit.ResourceTestRule;

import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mockito;

import co.je.conductor.domain.business.JobBusiness;
import co.je.conductor.domain.entities.JobRequest;
import co.je.conductor.infrastructure.exceptions.IException;
import co.je.conductor.infrastructure.exceptions.TechnicalException;
import co.je.conductor.utils.JobRequestFactoryForTests;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource.Builder;

public class JobRequestResourceTest {

    private static final JobBusiness jobBusinessMock = Mockito.mock(JobBusiness.class);
    private static final JobRequestResource jobRequestResource = new JobRequestResource(jobBusinessMock);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
    .addResource(jobRequestResource).build();

    private Builder getDefaultHttpClient(String uri) {

        return resources.client().resource(uri)
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON);
    }

    @Test
    public void testCreateJobRequest_OK() {

        String uri = "/jobs";
        JobRequest jobRequest = JobRequestFactoryForTests.getJobRequest();

        String expectedJobId = "234";
        Either<IException, String> createdJobRequestIdEither = Either.right(expectedJobId);
        Mockito.when(jobBusinessMock.createJobRequest(Mockito.any(JobRequest.class))).thenReturn(createdJobRequestIdEither);

        ClientResponse httpResponse = getDefaultHttpClient(uri).post(ClientResponse.class, jobRequest);
        assertNotNull(httpResponse);

        int statusCode = httpResponse.getStatus();
        assertEquals(201, statusCode);

        try {

            Map<String, Object> mapResponse = httpResponse.getEntity(Map.class);
            assertNotNull(mapResponse);
            assertEquals(expectedJobId, mapResponse.get("jobID"));

        } catch (Exception e) {

            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testCreateJobRequest_NOK_failedCreatingJobRequest() {

        String uri = "/jobs";
        JobRequest jobRequest = JobRequestFactoryForTests.getJobRequest();

        IException technicalException = new TechnicalException("The job request could not be created.");
        Either<IException, String> createdJobRequestIdEither = Either.left(technicalException);
        Mockito.when(jobBusinessMock.createJobRequest(Mockito.any(JobRequest.class))).thenReturn(createdJobRequestIdEither);

        ClientResponse httpResponse = getDefaultHttpClient(uri).post(ClientResponse.class, jobRequest);
        assertNotNull(httpResponse);

        int statusCode = httpResponse.getStatus();
        assertEquals(500, statusCode);

        try {

            IException exception = httpResponse.getEntity(IException.class);
            assertNotNull(exception);
            assertEquals(0, technicalException.compareTo(exception));

        } catch (Exception e) {

            fail("Unexpected exception: " + e.getMessage());
        }
    }
}