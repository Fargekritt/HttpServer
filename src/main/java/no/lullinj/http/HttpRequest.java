package no.lullinj.http;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class HttpRequest {

    private final Map<String, List<String>>  headers;
    private final String body;

    private final String method;

    private final String uri;


    public HttpRequest(Map<String, List<String>> headers, String body, String method, String uri){
        this.headers = headers;
        this.body = body;
        this.method = method;
        this.uri = uri;
    }






    public String getBody() {
        return body;
    }

    public List<String> getHeader(String header){
        return headers.get(header.toLowerCase(Locale.ROOT));
    }
    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

}
