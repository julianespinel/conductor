package co.je.conductor.domain.business;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;

import com.mongodb.DB;

import co.je.conductor.domain.entities.ConcurrencySpecs;
import co.je.conductor.domain.entities.HttpRequestSpecs;
import co.je.conductor.domain.entities.JobRequest;
import co.je.conductor.domain.entities.JobResult;
import co.je.conductor.persistence.daos.JobResultDAO;

public class JobExecutor implements Runnable {

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

    private static List<JobExecutorWorker> createJobWorkers(int totalCalls, HttpRequestSpecs httpRequestSpecs,
            List<String> payloadList) {

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

    private List<Future<HttpResponse>> executeJobInParallel(ExecutorService threadPool, List<JobExecutorWorker> workers) {

        List<Future<HttpResponse>> httpResponseList = new ArrayList<Future<HttpResponse>>();

        for (JobExecutorWorker worker : workers) {

            Future<HttpResponse> httpResponse = threadPool.submit(worker);
            httpResponseList.add(httpResponse);
        }

        return httpResponseList;
    }

    private List<HttpResponse> resolveFutures(List<Future<HttpResponse>> futuresList) throws InterruptedException,
            ExecutionException {

        List<HttpResponse> httpResponsesList = new ArrayList<HttpResponse>();

        for (Future<HttpResponse> future : futuresList) {
            httpResponsesList.add(future.get());
        }

        return httpResponsesList;
    }

    @Override
    public void run() {

        try {

            ConcurrencySpecs concurrencySpecs = jobRequest.getConcurrencySpecs();

            int totalCalls = concurrencySpecs.getTotalCalls();
            int concurrentCalls = concurrencySpecs.getConcurrentCalls();
            ExecutorService threadPool = Executors.newFixedThreadPool(concurrentCalls);

            HttpRequestSpecs httpRequestSpecs = jobRequest.getHttpRequestSpecs();

            List<JobExecutorWorker> workers = createJobWorkers(totalCalls, httpRequestSpecs, payloadList);
            List<Future<HttpResponse>> futuresList = executeJobInParallel(threadPool, workers);

            String jobRequestID = jobRequest.getId();
            List<HttpResponse> httpResponsesList = resolveFutures(futuresList);
            JobResult jobResult = new JobResult(jobRequestID, httpResponsesList);
            jobResultDAO.saveJobResult(mongoDB, jobResult);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}