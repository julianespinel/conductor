package co.je.conductor.domain.business;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import co.je.conductor.domain.entities.ExecutionSpecs;
import co.je.conductor.domain.entities.HttpRequestSpecs;
import co.je.conductor.domain.entities.JobRequest;
import co.je.conductor.domain.exceptions.UnsupportedJsonNodeException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
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

    private static String getModifiedJsonPayload(JsonNode httpPayload, List<String> payloadKeysToModify, int index) throws UnsupportedJsonNodeException {

        for (int i = 0; i < payloadKeysToModify.size(); i++) {

            String key = payloadKeysToModify.get(i);
            String[] keysArray = key.split("\\.");
            fj.data.List<String> nestedKeys = fj.data.List.list(keysArray);

            JsonNode leafJsonNode = getLeafJsonNode(httpPayload, nestedKeys);
            
            String keyToChange = nestedKeys.last();
            JsonNode nodeToChange = leafJsonNode.get(keyToChange);
            JsonNode valueNode = getModifiedValueNode(nodeToChange, index);
            
            ObjectNode objectNode = (ObjectNode) leafJsonNode;
            objectNode.set(keyToChange, valueNode);
        }
        
        System.out.println(httpPayload);

        return httpPayload.toString();
    }

	private static JsonNode getModifiedValueNode(JsonNode nodeToChange, int index) throws UnsupportedJsonNodeException {
		
		JsonNode valueNode = null;
		
		if (nodeToChange.isNull() || nodeToChange.isObject()) {
			
			String message = "Null or Object type JSON nodes are not supported.";
			throw new UnsupportedJsonNodeException(message);
			
		} else if (nodeToChange.isBoolean()) {
			
			boolean booleanValue = nodeToChange.asBoolean();
			boolean modifiedBooleanValue = booleanValue;
			
			if (index % 2 == 0) {
				modifiedBooleanValue = !booleanValue;
			}
			
			valueNode = JsonNodeFactory.instance.booleanNode(modifiedBooleanValue);
			
		} else if (nodeToChange.isArray()) {
			
			ArrayNode modifiedJsonArray = getModifiedJsonArray(nodeToChange);
			valueNode = modifiedJsonArray;
			
		} else if (nodeToChange.isNumber()) {
			
			valueNode = getModifiedNumber(nodeToChange, index);
			
		} else if (nodeToChange.isTextual()) {
			
			String value = nodeToChange.asText();
			String modifiedValue = value + index;
			valueNode = JsonNodeFactory.instance.textNode(modifiedValue);
		}
		return valueNode;
	}

	private static ArrayNode getModifiedJsonArray(JsonNode nodeToChange) throws UnsupportedJsonNodeException {
		
		ArrayNode modifiedJsonArray = JsonNodeFactory.instance.arrayNode();
		Iterator<JsonNode> elements = nodeToChange.elements();
		
		int whileIndex = 0;
		while (elements.hasNext()) {
			
			JsonNode element = elements.next();
			JsonNode modifiedElementNode = getModifiedValueNode(element, whileIndex);
			modifiedJsonArray.add(modifiedElementNode);
			whileIndex++;
		}
		
		return modifiedJsonArray;
	}

	private static JsonNode getModifiedNumber(JsonNode nodeToChange, int index) {
		
		JsonNode valueNode = null;
		
		if (nodeToChange.isNumber()) {
			
			if (nodeToChange.isBigDecimal()) {
				
				BigDecimal originalValue = nodeToChange.decimalValue();
				BigDecimal modifiedValue = originalValue.add(BigDecimal.valueOf(index));
				valueNode = JsonNodeFactory.instance.numberNode(modifiedValue);
				
			} else if (nodeToChange.isBigInteger()) {
				
				BigInteger originalValue = nodeToChange.bigIntegerValue();
				BigInteger modifiedValue = originalValue.add(BigInteger.valueOf(index));
				valueNode = JsonNodeFactory.instance.numberNode(modifiedValue);
				
			} else if (nodeToChange.isDouble()) {
				
				double originalValue = nodeToChange.doubleValue();
				double modifiedValue = originalValue + index;
				valueNode = JsonNodeFactory.instance.numberNode(modifiedValue);
				
			} else if (nodeToChange.isFloat()) {
				
				float originalValue = nodeToChange.floatValue();
				float modifiedValue = originalValue + index;
				valueNode = JsonNodeFactory.instance.numberNode(modifiedValue);
				
			} else if (nodeToChange.isInt()) {
				
				int originalValue = nodeToChange.intValue();
				int modifiedValue = originalValue + index;
				valueNode = JsonNodeFactory.instance.numberNode(modifiedValue);
				
			} else if (nodeToChange.isLong()) {
				
				long originalValue = nodeToChange.longValue();
				long modifiedValue = originalValue + index;
				valueNode = JsonNodeFactory.instance.numberNode(modifiedValue);
			}
			
		} else {
			
			String message = "The given JSON node is not a number node.";
			throw new IllegalArgumentException(message);
		}
		
		return valueNode;
	}

	public static List<String> generatePayloadList(JobRequest correctedJobRequest) throws UnsupportedJsonNodeException {

        List<String> payloadList = new ArrayList<String>();

        ExecutionSpecs executionSpecs = correctedJobRequest.getExecutionSpecs();
        HttpRequestSpecs httpRequestSpecs = correctedJobRequest.getHttpRequestSpecs();
        List<String> payloadKeysToModify = correctedJobRequest.getPayloadKeysToModify();

        String httpMethod = httpRequestSpecs.getHttpMethod();
        boolean isPostOrPutRequest = HttpValidator.isPostOrPutRequest(httpMethod);

        JsonNode httpPayload = httpRequestSpecs.getHttpPayload();
        boolean requestHasPayload = HttpValidator.requestHasPayload(httpPayload);

        if (isPostOrPutRequest && requestHasPayload && !payloadKeysToModify.isEmpty()) {

            int totalCalls = executionSpecs.getTotalCalls();

            for (int i = 0; i < totalCalls; i++) {

                JsonNode httpPayloadCopy = httpPayload.deepCopy();
                String modifiedJsonPayload = getModifiedJsonPayload(httpPayloadCopy, payloadKeysToModify, i);
                payloadList.add(modifiedJsonPayload);
            }
        }

        return payloadList;
    }
}