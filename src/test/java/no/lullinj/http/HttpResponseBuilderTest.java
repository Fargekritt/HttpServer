package no.lullinj.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpResponseBuilderTest {

    @Test
    void testStatusMessageIsCorrect() {

        HttpResponseBuilder responseBuilder = new HttpResponseBuilder();

        responseBuilder.setStatusCode(200);
        HttpResponse response = responseBuilder.buid();
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.getStatusMessage(), "OK");

        responseBuilder.setStatusCode(300);
        response = responseBuilder.buid();
        assertEquals(response.getStatusCode(), 300);
        assertEquals(response.getStatusMessage(), "MULTIPLE CHOICES");

        responseBuilder.setStatusCode(400);
        response = responseBuilder.buid();
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.getStatusMessage(), "BAD REQUEST");

        responseBuilder.setStatusCode(500);
        response = responseBuilder.buid();
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.getStatusMessage(), "INTERNAL SERVER ERROR");

    }


}