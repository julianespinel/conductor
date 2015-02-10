package co.je.conductor.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;

import co.je.conductor.domain.entities.HttpRequestSpecs;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class HttpRequestSpecsFactoryForTests {
    
    public static Map<String, String> getHttpHeaders() {

        Map<String, String> httpHeaders = new HashMap<String, String>();
        httpHeaders.put(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString());
        httpHeaders.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
        return httpHeaders;
    }
    
    public static ObjectNode getHttpPayload() {

        ObjectNode httpPayload = JsonNodeFactory.instance.objectNode();
        httpPayload.put("id", "123");
        httpPayload.put("name", "julian");
        httpPayload.put("username", "j");
        httpPayload.put("password", "qwe123");

        ObjectNode locationObjectNode = JsonNodeFactory.instance.objectNode();

        locationObjectNode.put("country", "US");
        locationObjectNode.put("State", "MA");
        locationObjectNode.put("city", "Boston");
        httpPayload.put("location", locationObjectNode);

        ObjectNode addressObjectNode = JsonNodeFactory.instance.objectNode();

        addressObjectNode.put("number", "30");
        addressObjectNode.put("street", "Orlando Ave");
        addressObjectNode.put("description", "big and beautiful grey house");
        locationObjectNode.put("address", addressObjectNode);

        return httpPayload;
    }

    public static HttpRequestSpecs getHttpRequestSpecs() {

        String httpMethod = "POST";
        String url = "www.library.com/books";
        Map<String, String> httpHeaders = getHttpHeaders();
        ObjectNode httpPayload = getHttpPayload();

        HttpRequestSpecs httpRequestSpecs = new HttpRequestSpecs(httpMethod, url, httpHeaders, httpPayload);
        return httpRequestSpecs;
    }
    
    public static List<String> getPayloadKeysToIterate() {

        List<String> payloadKeysToIterate = new ArrayList<String>();
        payloadKeysToIterate.add("id");
        payloadKeysToIterate.add("username");
        payloadKeysToIterate.add("location.city");
        payloadKeysToIterate.add("location.address.number");

        return payloadKeysToIterate;
    }
}