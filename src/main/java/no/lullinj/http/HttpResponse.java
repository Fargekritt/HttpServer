package no.lullinj.http;

import java.util.List;
import java.util.Map;

public class HttpResponse {

    private final Map<String, List<String>> headers;
    private String body;
    private String statusMessage;

    private int statusCode;

    public HttpResponse(Map<String, List<String>> headers, String body, String statusMessage, int statusCode) {

        this.headers = headers;
        this.body = body;
        this.statusMessage = statusMessage;
        this.statusCode = statusCode;
    }

    public HttpResponse(Map<String, List<String>> headers, String statusMessage, int statusCode) {

        this.headers = headers;
        this.body = "";
        this.statusMessage = statusMessage;
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
