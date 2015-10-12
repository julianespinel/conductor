package co.je.conductor.domain.business;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.je.conductor.domain.entities.ExecutionSpecs;
import co.je.conductor.domain.entities.HTTPConductorResponse;
import co.je.conductor.domain.entities.HttpRequestSpecs;
import co.je.conductor.domain.entities.JobRequest;
import co.je.conductor.domain.entities.JobResult;
import co.je.conductor.persistence.daos.JobResultDAO;

import com.mongodb.DB;

public class JobExecutor implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobExecutor.class);

    private final DB mongoDB;
    private final JobResultDAO jobResultDAO;
    private final JobRequest jobRequest;
    private final List<String> payloadList;

    public JobExecutor(DB mongoDB, JobResultDAO jobResultDAO, JobRequest correctedJobRequest, List<String> payloadList) {

        this.mongoDB = mongoDB;
        this.jobResultDAO = jobResultDAO;
        jobRequest = correctedJobRequest;
        this.payloadList = payloadList;
    }

    private static List<JobExecutorWorker> createJobWorkers(int totalCalls, HttpRequestSpecs httpRequestSpecs, List<String> payloadList) {

        List<JobExecutorWorker> workers = new ArrayList<JobExecutorWorker>();

        boolean payloadListIsEmpty = payloadList.isEmpty();
        boolean totalCallsMatchPayloadListSize = (totalCalls == payloadList.size());
        boolean shouldCreateWorkers = payloadListIsEmpty ? true : totalCallsMatchPayloadListSize;

        String emptyPayload = "{}";

        for (int i = 0; i < totalCalls && shouldCreateWorkers; i++) {

            String payload = payloadListIsEmpty ? emptyPayload : payloadList.get(i);
            JobExecutorWorker worker = new JobExecutorWorker(httpRequestSpecs, payload);
            workers.add(worker);
        }

        return workers;
    }

    private List<Future<HTTPConductorResponse>> executeJobInParallel(ExecutorService threadPool, List<JobExecutorWorker> workers) {

        List<Future<HTTPConductorResponse>> httpConductorResponseList = new ArrayList<Future<HTTPConductorResponse>>();

        for (JobExecutorWorker worker : workers) {

            Future<HTTPConductorResponse> httpConductorResponse = threadPool.submit(worker);
            httpConductorResponseList.add(httpConductorResponse);
        }

        return httpConductorResponseList;
    }

    private List<HTTPConductorResponse> resolveFutures(List<Future<HTTPConductorResponse>> futuresList) {

        List<HTTPConductorResponse> httpConductorResponseList = new ArrayList<HTTPConductorResponse>();

        for (Future<HTTPConductorResponse> future : futuresList) {

            try {
                httpConductorResponseList.add(future.get());

            } catch (Exception e) {

                LOGGER.error("resolveFutures", e);
            }
        }

        return httpConductorResponseList;
    }

    @Override
    public void run() {

        LOGGER.info("running jobRequest: " + jobRequest.getId());

        ExecutionSpecs executionSpecs = jobRequest.getExecutionSpecs();

        int totalCalls = executionSpecs.getTotalCalls();
        int concurrentCalls = executionSpecs.getParallelCalls();
        ExecutorService threadPool = Executors.newFixedThreadPool(concurrentCalls);

        LOGGER.info("threadPool created, size: " + concurrentCalls);

        try {

            HttpRequestSpecs httpRequestSpecs = jobRequest.getHttpRequestSpecs();
            List<JobExecutorWorker> workers = createJobWorkers(totalCalls, httpRequestSpecs, payloadList);
            List<Future<HTTPConductorResponse>> futuresList = executeJobInParallel(threadPool, workers);

            LOGGER.info("workers created and jobs is being executed.");

            String jobRequestID = jobRequest.getId();
            List<HTTPConductorResponse> httpConductorResponseList = resolveFutures(futuresList);
            JobResult jobResult = new JobResult(jobRequestID, httpConductorResponseList);
            jobResultDAO.saveJobResult(mongoDB, jobResult);

            LOGGER.info("saved jobResult of the jobRequest: " + jobRequestID);

        } catch (Exception e) {

            LOGGER.error("run", e);

        } finally {

            threadPool.shutdown();
        }
    }
}