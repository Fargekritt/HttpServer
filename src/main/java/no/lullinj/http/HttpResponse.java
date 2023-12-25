package no.lullinj.http;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HttpResponse {

    private final Map<String, List<String>> headers;
    private byte[] body;
    private String statusMessage;

    private int statusCode;

    public HttpResponse(Map<String, List<String>> headers, String body, String statusMessage, int statusCode) {

        this.headers = headers;
        this.body = body.getBytes();
        this.statusMessage = statusMessage;
        this.statusCode = statusCode;
    }

    public HttpResponse(Map<String, List<String>> headers, byte[] body, String statusMessage, int statusCode) {

        this.headers = headers;
        this.body = body;
        this.statusMessage = statusMessage;
        this.statusCode = statusCode;
    }

    public HttpResponse(Map<String, List<String>> headers, String statusMessage, int statusCode) {

        this.headers = headers;
        this.body = "".getBytes();
        this.statusMessage = statusMessage;
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public List<String> getHeader(String header){
        return headers.get(header.toLowerCase(Locale.ROOT));
    }

    public byte[] getBody() {
        return body;
    }
}
