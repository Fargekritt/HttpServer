package no.lullinj.http;

import java.util.List;
import java.util.Map;

public class HttpResponseBuilder {

    private String body;

    private int statusCode;

    private String statusString;

    private Map<String, List<String>> headers;
}
