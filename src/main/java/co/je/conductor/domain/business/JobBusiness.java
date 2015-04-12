package co.je.conductor.domain.business;

import java.util.List;

import co.je.conductor.domain.entities.ConcurrencySpecs;
import co.je.conductor.domain.entities.HttpRequestSpecs;
import co.je.conductor.domain.entities.JobRequest;
import co.je.conductor.domain.exceptions.UnsupportedJsonNodeException;
import co.je.conductor.persistence.daos.JobRequestDAO;
import co.je.conductor.persistence.daos.JobResultDAO;

import com.mongodb.DB;

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

		// Correct concurrency specification in case of errors.
		ConcurrencySpecs concurrencySpecs = jobRequest.getConcurrencySpecs();
		ConcurrencySpecs correctedConcurrencySpecs = ConcurrencyValidator.getCorrectedConcurrencySpecs(concurrencySpecs);

		HttpRequestSpecs httpRequestSpecs = jobRequest.getHttpRequestSpecs();
		List<String> payloadKeysToModify = jobRequest.getPayloadKeysToModify();

		return new JobRequest(creatorEmail, correctedConcurrencySpecs, httpRequestSpecs, payloadKeysToModify);
	}

	public String createJobRequest(JobRequest jobRequest) throws UnsupportedJsonNodeException {

		JobRequest correctedJobRequest = getCorrectedJobRequest(jobRequest);
		String jobRequestId = jobRequestDAO.createJobRequest(mongoDB, correctedJobRequest);
		
		correctedJobRequest = new JobRequest(jobRequestId, correctedJobRequest);
		List<String> generatedPayloads = JsonPayloadFactory.generatePayloadList(correctedJobRequest);
		JobExecutor jobExecutor = new JobExecutor(mongoDB, jobResultDAO, correctedJobRequest, generatedPayloads);
		jobExecutor.run();

		return jobRequestId;
	}
}