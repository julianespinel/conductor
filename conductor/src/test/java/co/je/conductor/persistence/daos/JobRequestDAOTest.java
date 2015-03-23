package co.je.conductor.persistence.daos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import co.je.conductor.domain.entities.JobRequest;
import co.je.conductor.infrastructure.exceptions.IException;
import co.je.conductor.utils.JobRequestFactoryForTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DB;
import com.mongodb.MongoClient;

import fj.data.Either;

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
        Either<IException, String> either = jobRequestDAO.createJobRequest(mongoDB, jobRequest);
        
        assertNotNull(either);
        assertEquals(true, either.isRight());
        
        String requestId = either.right().value();
        assertEquals(false, StringUtils.isBlank(requestId));
    }
    
    @Test
    public void testCreateJobRequest_NOK() {
        
        JobRequest nullJobRequest = null;
        Either<IException, String> jobRequestIdeither = jobRequestDAO.createJobRequest(mongoDB, nullJobRequest);
        
        assertNotNull(jobRequestIdeither);
        assertEquals(false, jobRequestIdeither.isRight());
        
        IException exception = jobRequestIdeither.left().value();
        assertEquals(false, exception.isBusinessException());
    }
}