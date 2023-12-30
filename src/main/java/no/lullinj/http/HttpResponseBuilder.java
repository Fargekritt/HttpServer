package no.lullinj.http;


import java.nio.charset.StandardCharsets;
import java.util.*;

public class HttpResponseBuilder {

    private byte[] body;

    private int statusCode;

    private String statusString;

    private final Map<String, List<String>> headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public void addHeader(String name, String value) {
        if (!headers.containsKey(name)) {
            headers.put(name, new ArrayList<>());
        }
        headers.get(name).add(value);

    }

    public void addHeader(String name, Collection<String> values) {
        if (!headers.containsKey(name)) {
            headers.put(name, new ArrayList<>());
        }
        headers.get(name).addAll(values);
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        statusString = StatusCode.getStatusCodeMessage(statusCode);
    }



    public void setBody(String body) {
        this.body = body.getBytes();
        headers.put("content-length", List.of(String.valueOf(body.getBytes().length)));
    }

    public void setBody(byte[] body) {
        this.body = body;
        headers.put("content-length", List.of(String.valueOf(body.length)));
    }


    public HttpResponse build(){
        if(body == null){
            return new HttpResponse(headers, statusString, statusCode);
        }
        return new HttpResponse(headers, new String(body, StandardCharsets.UTF_8), statusString, statusCode);
    }
}
