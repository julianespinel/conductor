package co.je.conductor.domain.business;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.je.conductor.domain.entities.JobRequest;
import co.je.conductor.utils.JobRequestFactoryForTests;

public class JsonPayloadFactoryTest {

    @Test
    public void testGeneratePayloadList_OK() {
        
        JobRequest jobRequest = JobRequestFactoryForTests.getJobRequest();
        
        int totalCalls = 10;
        assertEquals(totalCalls, jobRequest.getConcurrencySpecs().getTotalCalls());
        
        int numberOfPayloadKeysToModify = 4;
        List<String> payloadKeysToModify = jobRequest.getPayloadKeysToModify();
        assertEquals(numberOfPayloadKeysToModify, payloadKeysToModify.size());
        
        String idKey = "id";
        String usernameKey = "username";
        String cityKey = "location.city";
        String addressNumberKey = "location.address.number";
        
        assertEquals(idKey, payloadKeysToModify.get(0));
        assertEquals(usernameKey, payloadKeysToModify.get(1));
        assertEquals(cityKey, payloadKeysToModify.get(2));
        assertEquals(addressNumberKey, payloadKeysToModify.get(3));
        
        List<String> generatedPayloadList = JsonPayloadFactory.generatePayloadList(jobRequest);
        
        assertNotNull(generatedPayloadList);
        assertEquals(totalCalls, generatedPayloadList.size());
        
        JsonNode originalPayload = jobRequest.getHttpRequestSpecs().getHttpPayload();
        assertNotNull(originalPayload);
        
        String id = originalPayload.get(idKey).asText();
        String username = originalPayload.get(usernameKey).asText();
        String city = originalPayload.get("location").get("city").asText();
        String addressNumber = originalPayload.get("location").get("address").get("number").asText();
        
        ObjectMapper objectMapper = new ObjectMapper();
        
        for (int i = 0; i < generatedPayloadList.size(); i++) {
            
            String generatedPayload = generatedPayloadList.get(i);
            
            try {
                
                JsonNode jsonNode = objectMapper.readValue(generatedPayload, JsonNode.class);
                
                String generatedId = jsonNode.get(idKey).asText();
                String generatedUsername = jsonNode.get(usernameKey).asText();
                String generatedCity = jsonNode.get("location").get("city").asText();
                String generatedAddressNumber = jsonNode.get("location").get("address").get("number").asText();
                
                int index = i;
                assertEquals(id + index, generatedId);
                assertEquals(username + index, generatedUsername);
                assertEquals(city + index, generatedCity);
                assertEquals(addressNumber + index, generatedAddressNumber);
                
            } catch (IOException e) {
                
                e.printStackTrace();
                fail("Unexpected exception: " + e.getMessage());
            }
        }
    }
}