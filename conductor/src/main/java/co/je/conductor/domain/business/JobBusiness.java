package co.je.conductor.domain.business;

import java.util.List;

import co.je.conductor.domain.entities.ConcurrencySpecs;
import co.je.conductor.domain.entities.HttpRequestSpecs;
import co.je.conductor.domain.entities.JobRequest;
import co.je.conductor.infrastructure.exceptions.IException;
import co.je.conductor.infrastructure.exceptions.TechnicalException;
import co.je.conductor.persistence.daos.JobRequestDAO;
import co.je.conductor.persistence.daos.JobResultDAO;

import com.mongodb.DB;

import fj.data.Either;

public class JobBusiness {

	private final DB mongoDB;
	private final JobRequestDAO jobRequestDAO;
	private final JobResultDAO jobResultDAO;

	public JobBusiness(DB mongoDB, JobRequestDAO jobRequestDAO, JobResultDAO jobResultDAO) {
		
		this.mongoDB = mongoDB;
		this.jobRequestDAO = jobRequestDAO;
		this.jobResultDAO = jobResultDAO;
	}

	private JobRequest getCorrectedJobRequest(JobRequest jobRequest) {

		String creatorEmail = jobRequest.getCreatorEmail();

		// Correct concurrency specification.
		ConcurrencySpecs concurrencySpecs = jobRequest.getConcurrencySpecs();
		ConcurrencySpecs correctedConcurrencySpecs = ConcurrencyValidator.getCorrectedConcurrencySpecs(concurrencySpecs);

		HttpRequestSpecs httpRequestSpecs = jobRequest.getHttpRequestSpecs();
		List<String> payloadKeysToModify = jobRequest.getPayloadKeysToModify();

		return new JobRequest(creatorEmail, correctedConcurrencySpecs, httpRequestSpecs, payloadKeysToModify);
	}

	public Either<IException, String> createJobRequest(JobRequest jobRequest) {

		Either<IException, String> jobRequestIdEither = null;

		JobRequest correctedJobRequest = getCorrectedJobRequest(jobRequest);
		List<String> payloadList = JsonPayloadFactory.generatePayloadList(correctedJobRequest);

		try {

			jobRequestIdEither = jobRequestDAO.createJobRequest(mongoDB, correctedJobRequest);
			
			if (jobRequestIdEither.isRight()) {
			    
			    String createdJobRequestID = jobRequestIdEither.right().value();
                correctedJobRequest = new JobRequest(createdJobRequestID, correctedJobRequest);
                
	            JobExecutor jobExecutor = new JobExecutor(mongoDB, jobResultDAO, correctedJobRequest, payloadList);
	            jobExecutor.run();
			}

		} catch (Exception e) {

		    e.printStackTrace();
			TechnicalException technicalException = new TechnicalException(e.getMessage());
			jobRequestIdEither = Either.left(technicalException);
		}

		return jobRequestIdEither;
	}
}