package no.lullinj.http;

import java.util.Map;
import java.util.TreeMap;

public class HttpResponse {

    private final Map<String, String> headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private  String body;
    private  String statusLine;

    public HttpResponse() {

    }
}
