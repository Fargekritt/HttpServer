package no.lullinj.http;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HttpRequest {

    private final Map<String, List<String>>  headers;
    private final char[] body;

    private final HttpMethod method;

    private final String uri;

    private final String version;

    private final List<Cookie> cookies;


    public HttpRequest(Map<String, List<String>> headers, char[] body, HttpMethod method, String uri, String version, List<Cookie> cookies){
        this.headers = headers;
        this.body = body;
        this.method = method;
        this.uri = uri;
        this.version = version;
        this.cookies = cookies;
    }






    public char[] getBody() {
        return body;
    }

    public List<String> getHeader(String header){
        return headers.get(header.toLowerCase(Locale.ROOT));
    }
    public HttpMethod getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getVersion() {
        return version;
    }

    public Cookie getCookie(String name) {
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals(name)){
                return cookie;
            }
        }
        return null;
    }
}
