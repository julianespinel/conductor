package co.je.conductor.domain.business;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import co.je.conductor.domain.entities.ConcurrencySpecs;
import co.je.conductor.domain.entities.HttpRequestSpecs;
import co.je.conductor.domain.entities.JobRequest;

import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.snakeyaml.nodes.NodeId;

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

    private static String getModifiedJsonPayload(JsonNode httpPayload, List<String> payloadKeysToModify, int index) {

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
            
//            System.out.println(nodeToChange);
//            System.out.println("*************************************** 1");
//            System.out.println("a " + nodeToChange.isArray());
//            System.out.println("b " + nodeToChange.isBigDecimal());
//            System.out.println("c " + nodeToChange.isBigInteger());
//            System.out.println("d " + nodeToChange.isBinary());
//            System.out.println("e " + nodeToChange.isContainerNode());
//            System.out.println("f " + nodeToChange.isDouble());
//            System.out.println("g " + nodeToChange.isFloat());
//            System.out.println("h " + nodeToChange.isFloatingPointNumber());
//            System.out.println("i " + nodeToChange.isInt());
//            System.out.println("j " + nodeToChange.isIntegralNumber());
//            System.out.println("k " + nodeToChange.isLong());
//            System.out.println("l " + nodeToChange.isMissingNode());
//            System.out.println("m " + nodeToChange.isNull());
//            System.out.println("n " + nodeToChange.isNumber());
//            System.out.println("o " + nodeToChange.isObject());
//            System.out.println("p " + nodeToChange.isPojo());
//            System.out.println("q " + nodeToChange.isShort());
//            System.out.println("r " + nodeToChange.isTextual());
//            System.out.println("s " + nodeToChange.isValueNode());
//            System.out.println("*************************************** 2");
        }
        
        System.out.println(httpPayload);

        return httpPayload.toString();
    }

	private static JsonNode getModifiedValueNode(JsonNode nodeToChange, int index) {
		
		JsonNode valueNode = null;
		
		if (nodeToChange.isNull() || nodeToChange.isObject()) {
			
			// TODO: error!
			
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

	private static ArrayNode getModifiedJsonArray(JsonNode nodeToChange) {
		
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
			
			// TODO: error!
		}
		
		return valueNode;
	}

	public static List<String> generatePayloadList(JobRequest correctedJobRequest) {

        List<String> payloadList = new ArrayList<String>();

        ConcurrencySpecs concurrencySpecs = correctedJobRequest.getConcurrencySpecs();
        HttpRequestSpecs httpRequestSpecs = correctedJobRequest.getHttpRequestSpecs();
        List<String> payloadKeysToModify = correctedJobRequest.getPayloadKeysToModify();

        String httpMethod = httpRequestSpecs.getHttpMethod();
        boolean isPostOrPutRequest = HttpValidator.isPostOrPutRequest(httpMethod);

        JsonNode httpPayload = httpRequestSpecs.getHttpPayload();
        boolean requestHasPayload = HttpValidator.requestHasPayload(httpPayload);

        if (isPostOrPutRequest && requestHasPayload && !payloadKeysToModify.isEmpty()) {

            int totalCalls = concurrencySpecs.getTotalCalls();

            for (int i = 0; i < totalCalls; i++) {

                JsonNode httpPayloadCopy = httpPayload.deepCopy();
                String modifiedJsonPayload = getModifiedJsonPayload(httpPayloadCopy, payloadKeysToModify, i);
                payloadList.add(modifiedJsonPayload);
            }
        }

        return payloadList;
    }
}