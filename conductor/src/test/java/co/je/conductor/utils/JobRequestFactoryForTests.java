package co.je.conductor.utils;

import java.util.List;

import co.je.conductor.domain.entities.ConcurrencySpecs;
import co.je.conductor.domain.entities.HttpRequestSpecs;
import co.je.conductor.domain.entities.JobRequest;

public class JobRequestFactoryForTests {

    public static JobRequest getJobRequest() {

        String id = "1";
        String creatorEmail = "user@domain.com";

        ConcurrencySpecs concurrencySpecs = ConcurrencySpecsFactoryForTests.getConcurrencySpecs();
        HttpRequestSpecs httpRequestSpecs = HttpRequestSpecsFactoryForTests.getHttpRequestSpecs();
        List<String> payloadKeysToIterate = HttpRequestSpecsFactoryForTests.getPayloadKeysToIterate();

        return new JobRequest(id, creatorEmail, concurrencySpecs, httpRequestSpecs, payloadKeysToIterate);
    }
}