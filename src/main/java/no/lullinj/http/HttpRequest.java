package no.lullinj.http;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HttpRequest {

    private final Map<String, List<String>>  headers;
    private final String body;

    private final String method;

    private final String uri;

    private final String version;


    public HttpRequest(Map<String, List<String>> headers, String body, String method, String uri, String version){
        this.headers = headers;
        this.body = body;
        this.method = method;
        this.uri = uri;
        this.version = version;
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

    public String getVersion() {
        return version;
    }
}
