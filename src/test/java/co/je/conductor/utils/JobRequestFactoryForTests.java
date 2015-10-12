package co.je.conductor.utils;

import java.util.List;

import co.je.conductor.domain.entities.ExecutionSpecs;
import co.je.conductor.domain.entities.HttpRequestSpecs;
import co.je.conductor.domain.entities.JobRequest;

public class JobRequestFactoryForTests {

    public static JobRequest getJobRequest() {

        String id = "1";
        String creatorEmail = "user@domain.com";

        ExecutionSpecs executionSpecs = ConcurrencySpecsFactoryForTests.getConcurrencySpecs();
        HttpRequestSpecs httpRequestSpecs = HttpRequestSpecsFactoryForTests.getHttpRequestSpecs();
        List<String> payloadKeysToModify = HttpRequestSpecsFactoryForTests.getPayloadKeysToModify();

        return new JobRequest(id, creatorEmail, executionSpecs, httpRequestSpecs, payloadKeysToModify);
    }
}