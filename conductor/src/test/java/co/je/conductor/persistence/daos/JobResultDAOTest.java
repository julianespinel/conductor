package co.je.conductor.persistence.daos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import co.je.conductor.domain.entities.JobResult;
import co.je.conductor.infrastructure.exceptions.IException;
import co.je.conductor.utils.JobResultFactoryForTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DB;
import com.mongodb.MongoClient;

import fj.data.Either;

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
        Either<IException, String> jobResultIdEither = jobResultDAO.saveJobResult(mongoDB, jobResult);
        
        assertNotNull(jobResultIdEither);
        assertEquals(true, jobResultIdEither.isRight());
        
        String jobResultId = jobResultIdEither.right().value();
        assertEquals(false, StringUtils.isBlank(jobResultId));
    }
    
    @Test
    public void testSaveJobResult_NOK() {
        
        JobResult nullJobResult = null;
        Either<IException, String> jobResultIdEither = jobResultDAO.saveJobResult(mongoDB, nullJobResult);
        
        assertNotNull(jobResultIdEither);
        assertEquals(false, jobResultIdEither.isRight());
        
        IException exception = jobResultIdEither.left().value();
        assertEquals(false, exception.isBusinessException());
    }
}