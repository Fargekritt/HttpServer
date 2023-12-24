package no.lullinj.http;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class HttpResponse {

    private final Map<String, List<String>> headers;
    private String body;
    private String statusLine;

    public HttpResponse(Map<String, List<String>> headers, String body, String statusLine) {

        this.headers = headers;
        this.body = body;
        this.statusLine = statusLine;
        Optional<String> optionalString = Optional.of("heik");
        optionalString.orElse("awd" + "hei");
    }
}
