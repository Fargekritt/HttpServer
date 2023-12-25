package no.lullinj.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpResponseBuilderTest {
    HttpResponseBuilder responseBuilder;
    @BeforeEach
    void setup(){

        responseBuilder = new HttpResponseBuilder();
    }
    @Test
    void testStatusMessageIsCorrect() {



        responseBuilder.setStatusCode(200);
        HttpResponse response = responseBuilder.build();
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.getStatusMessage(), "OK");

        responseBuilder.setStatusCode(300);
        response = responseBuilder.build();
        assertEquals(response.getStatusCode(), 300);
        assertEquals(response.getStatusMessage(), "MULTIPLE CHOICES");

        responseBuilder.setStatusCode(400);
        response = responseBuilder.build();
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.getStatusMessage(), "BAD REQUEST");

        responseBuilder.setStatusCode(500);
        response = responseBuilder.build();
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.getStatusMessage(), "INTERNAL SERVER ERROR");

    }

    @Test
    void testContentLength(){
        String exceptedBody = "hei alle sammen";
        int exceptedContentLength = exceptedBody.length();

        responseBuilder.setBody(exceptedBody);
        responseBuilder.setStatusCode(200);
        HttpResponse response = responseBuilder.build();

        String actualBody = new String(response.getBody());
        int actualContentLength = Integer.parseInt(response.getHeader("content-length").getFirst());

        assertEquals(actualBody, exceptedBody);
        assertEquals(actualContentLength,exceptedContentLength);
    }


}