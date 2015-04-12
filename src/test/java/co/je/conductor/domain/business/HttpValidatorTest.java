package co.je.conductor.domain.business;

import static org.junit.Assert.*;

import org.eclipse.jetty.http.HttpMethod;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public class HttpValidatorTest {
    
    @Test
    public void testIsPostOrPutRequest_OK() {
        
        String post = HttpMethod.POST.name();
        assertEquals(true, HttpValidator.isPostOrPutRequest(post)); 
        
        String put = "PUT";
        assertEquals(true, HttpValidator.isPostOrPutRequest(put));
        
        String postMixedCase = "PoSt";
        assertEquals(true, HttpValidator.isPostOrPutRequest(postMixedCase));
        
        String putMixedCase = "pUt";
        assertEquals(true, HttpValidator.isPostOrPutRequest(putMixedCase));
        
        String get = "GET";
        assertEquals(false, HttpValidator.isPostOrPutRequest(get));
        
        String delete = "delete";
        assertEquals(false, HttpValidator.isPostOrPutRequest(delete));
        
        String anyString = "hallo";
        assertEquals(false, HttpValidator.isPostOrPutRequest(anyString));
    }
    
    @Test
    public void testIsPostOrPutRequest_NOK_nullMethod() {
        
        String nullString = null;
        assertEquals(false, HttpValidator.isPostOrPutRequest(nullString)); 
    }
    
    @Test
    public void testIsPostOrPutRequest_NOK_emptyMethod() {
        
        String emptyString = "";
        assertEquals(false, HttpValidator.isPostOrPutRequest(emptyString)); 
    }
    
    @Test
    public void testRequestHasPayload_OK() {
        
        JsonNodeFactory jsonNodeFactoryInstance = JsonNodeFactory.instance;
        
        JsonNode numberNode = jsonNodeFactoryInstance.numberNode(1);
        assertEquals(true, HttpValidator.requestHasPayload(numberNode));
        
        JsonNode booleanNode = jsonNodeFactoryInstance.booleanNode(false);
        assertEquals(true, HttpValidator.requestHasPayload(booleanNode));
        
        JsonNode binaryNode = jsonNodeFactoryInstance.binaryNode("hallo".getBytes());
        assertEquals(true, HttpValidator.requestHasPayload(binaryNode));
        
        JsonNode arrayNode = jsonNodeFactoryInstance.arrayNode();
        assertEquals(true, HttpValidator.requestHasPayload(arrayNode));
        
        JsonNode nullNode = jsonNodeFactoryInstance.nullNode();
        assertEquals(false, HttpValidator.requestHasPayload(nullNode));
    }
    
    @Test
    public void testRequestHasPayload_NOK_nullPayload() {
        
        JsonNode nullObject = null;
        assertEquals(false, HttpValidator.requestHasPayload(nullObject));
    }
}