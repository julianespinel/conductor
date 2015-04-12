package co.je.conductor.persistence.daos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import co.je.conductor.domain.entities.JobRequest;
import co.je.conductor.utils.JobRequestFactoryForTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DB;
import com.mongodb.MongoClient;

public class JobRequestDAOTest {

    private final static String TEST_DB = "conductor_test_db";

    private static MongoClient mongoClient;
    private static DB mongoDB;
    
    private ObjectMapper objectMapper;
    private JobRequestDAO jobRequestDAO;

    @BeforeClass
    public static void setUpClass() {

        try {

            mongoClient = new MongoClient();
            mongoDB = mongoClient.getDB(TEST_DB);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
    @AfterClass
    public static void tearDownClass() {
        
        mongoDB.dropDatabase();
        mongoClient.close();
        mongoDB = null;
        mongoClient = null;
    }

    @Before
    public void setUp() {

        objectMapper = new ObjectMapper();
        jobRequestDAO = new JobRequestDAO(objectMapper);
    }

    @After
    public void tearDown() {

        jobRequestDAO = null;
        objectMapper = null;
        mongoDB.getCollection(JobRequestDAO.JOBS_REQUESTS_COLLECTION).drop();
    }

    @Test
    public void testCreateJobRequest_OK() {
        
        JobRequest jobRequest = JobRequestFactoryForTests.getJobRequest();
        String createdJobRequestId = jobRequestDAO.createJobRequest(mongoDB, jobRequest);
        assertEquals(false, StringUtils.isBlank(createdJobRequestId));
    }
    
    @Test
    public void testCreateJobRequest_NOK_JsonProcessingException() {
        
        try {
        	
        	objectMapper = Mockito.mock(ObjectMapper.class);
        	jobRequestDAO = new JobRequestDAO(objectMapper);
        	
        	JobRequest nullJobRequest = null;
        	Mockito.doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(nullJobRequest);
        	
        	jobRequestDAO.createJobRequest(mongoDB, nullJobRequest);
        	fail("Method should throw an IllegalArgumentException");
			
		} catch (JsonProcessingException | IllegalArgumentException e) {
			
			assertNotNull(e);
		}
    }
}