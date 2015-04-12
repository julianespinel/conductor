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

import co.je.conductor.domain.entities.JobResult;
import co.je.conductor.utils.JobResultFactoryForTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DB;
import com.mongodb.MongoClient;

public class JobResultDAOTest {
    
    private final static String TEST_DB = "conductor_test_db";

    private static MongoClient mongoClient;
    private static DB mongoDB;
    
    private ObjectMapper objectMapper;
    private JobResultDAO jobResultDAO;
    
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
        jobResultDAO = new JobResultDAO(objectMapper);
    }

    @After
    public void tearDown() {

        jobResultDAO = null;
        objectMapper = null;
        mongoDB.getCollection(JobResultDAO.JOBS_RESULTS_COLLECTION).drop();
    }

    @Test
    public void testSaveJobResult_OK() {
        
        int listSize = 5;
        JobResult jobResult = JobResultFactoryForTests.getJobResult(listSize);
        String jobResultId = jobResultDAO.saveJobResult(mongoDB, jobResult);
        
        assertEquals(false, StringUtils.isBlank(jobResultId));
    }
    
    @Test
    public void testSaveJobResult_NOK_JsonProcessingException() {
    	
    	try {
    		
    		objectMapper = Mockito.mock(ObjectMapper.class);
        	jobResultDAO = new JobResultDAO(objectMapper);
            
            JobResult nullJobResult = null;
            Mockito.doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(nullJobResult);
            
            jobResultDAO.saveJobResult(mongoDB, nullJobResult);
            fail("The method should throw an IllegalArgumentException.");
			
		} catch (JsonProcessingException | IllegalArgumentException e) {
			
			assertNotNull(e);
		}
    }
}