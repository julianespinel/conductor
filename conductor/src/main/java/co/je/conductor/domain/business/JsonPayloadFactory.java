package co.je.conductor.domain.business;

import java.util.ArrayList;
import java.util.List;

import co.je.conductor.domain.entities.ConcurrencySpecs;
import co.je.conductor.domain.entities.HttpRequestSpecs;
import co.je.conductor.domain.entities.JobRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonPayloadFactory {

    private static JsonNode getLeafJsonNode(JsonNode parentJson, fj.data.List<String> nestedKeys) {

        JsonNode leafJsonNode = null;

        if (nestedKeys.length() > 1) {

            JsonNode childJson = parentJson.get(nestedKeys.head());
            leafJsonNode = getLeafJsonNode(childJson, nestedKeys.tail());

        } else {

            leafJsonNode = parentJson;
        }

        return leafJsonNode;
    }

    private static String getModifiedJsonPayload(JsonNode httpPayload, List<String> payloadKeysToIterate, int index) {

        for (int i = 0; i < payloadKeysToIterate.size(); i++) {

            String key = payloadKeysToIterate.get(i);
            String[] keysArray = key.split("\\.");
            fj.data.List<String> nestedKeys = fj.data.List.list(keysArray);

            JsonNode leafJsonNode = getLeafJsonNode(httpPayload, nestedKeys);

            String keyToChange = nestedKeys.last();
            String value = leafJsonNode.get(keyToChange).asText();
            String modifiedValue = value + index;

            ObjectNode objectNode = (ObjectNode) leafJsonNode;
            objectNode.put(keyToChange, modifiedValue);
        }

        return httpPayload.toString();
    }

    public static List<String> generatePayloadList(JobRequest correctedJobRequest) {

        List<String> payloadList = new ArrayList<String>();

        ConcurrencySpecs concurrencySpecs = correctedJobRequest.getConcurrencySpecs();
        HttpRequestSpecs httpRequestSpecs = correctedJobRequest.getHttpRequestSpecs();
        List<String> payloadKeysToIterate = correctedJobRequest.getPayloadKeysToIterate();

        String httpMethod = httpRequestSpecs.getHttpMethod();
        boolean isPostOrPutRequest = HttpValidator.isPostOrPutRequest(httpMethod);

        JsonNode httpPayload = httpRequestSpecs.getHttpPayload();
        boolean requestHasPayload = HttpValidator.requestHasPayload(httpPayload);

        if (isPostOrPutRequest && requestHasPayload && !payloadKeysToIterate.isEmpty()) {

            int totalCalls = concurrencySpecs.getTotalCalls();

            for (int i = 0; i < totalCalls; i++) {

                JsonNode httpPayloadCopy = httpPayload.deepCopy();
                String modifiedJsonPayload = getModifiedJsonPayload(httpPayloadCopy, payloadKeysToIterate, i);
                payloadList.add(modifiedJsonPayload);
            }
        }

        return payloadList;
    }
}