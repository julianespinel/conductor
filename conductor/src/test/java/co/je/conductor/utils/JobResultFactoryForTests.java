package co.je.conductor.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.bson.types.ObjectId;

import co.je.conductor.domain.entities.JobResult;

public class JobResultFactoryForTests {

    private static HttpResponse createHttpResponse(HttpResponseFactory httpResponseFactory) {

        ProtocolVersion protocolVersion = new HttpVersion(1, 1);
        int statusCode = 200;
        String reasonPhrase = "OK";
        StatusLine statusline = new BasicStatusLine(protocolVersion, statusCode, reasonPhrase);

        HttpContext context = new BasicHttpContext();

        return httpResponseFactory.newHttpResponse(statusline, context);
    }

    private static List<HttpResponse> getHttpResponseList(int httpResponseListSize) {

        List<HttpResponse> httpResponseList = new ArrayList<HttpResponse>();

        HttpResponseFactory httpResponseFactory = new DefaultHttpResponseFactory();

        for (int i = 0; i < httpResponseListSize; i++) {

            HttpResponse newHttpResponse = createHttpResponse(httpResponseFactory);
            httpResponseList.add(newHttpResponse);
        }

        return httpResponseList;
    }

    public static JobResult getJobResult(int httpResponseListSize) {

        String id = ObjectId.get().toString();
        String jobRequestId = ObjectId.get().toString();
        List<HttpResponse> httpResponsesList = getHttpResponseList(httpResponseListSize);

        return new JobResult(id, jobRequestId, httpResponsesList);
    }
}