package no.lullinj.http;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HttpRequestParser {

    private Map<String, List<String>> headers;
    private String body;
    private HttpMethod method;
    private String uri;
    private String version;


    public HttpRequest parseHttpRequest(InputStream inputStream) throws InvalidHttpRequestException {
        BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));

        parseStatusLine(input);
        parseHeaders(input);
        parseBody(input);


        return new HttpRequest(headers, body, method, uri, version);
    }

    private void parseBody(BufferedReader input) throws InvalidHttpRequestException {
        if (getContentLength() < 1 || method.equals(HttpMethod.GET)) {
            body = "";
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        try {
            int contentLength = getContentLength();
            char[] buffer = new char[contentLength];
            int readBytesLength = input.read(buffer);
            if (readBytesLength < getContentLength()){
                throw new InvalidHttpRequestException("Body too short, Content-Length:" + getContentLength() + " Body Length: "  +readBytesLength);
            }
            stringBuilder.append(buffer);
            body = stringBuilder.toString();
        } catch (IOException e) {
            throw new InvalidHttpRequestException(e);
        }


    }

    private void parseStatusLine(BufferedReader input) throws InvalidHttpRequestException {
        String[] statusLine = readLine(input).split(" ");
        if (statusLine.length != 3) {
            throw new InvalidHttpRequestException("Invalid Status line: " + Arrays.toString(statusLine));
        }
        try{

            method = HttpMethod.valueOf(statusLine[0]);
            uri = statusLine[1];
            version = statusLine[2];
        } catch (IllegalArgumentException e){
            throw new InvalidHttpRequestException("Invalid method \"" + statusLine[0] +"\"");
        }
    }


    private String readLine(BufferedReader input) throws InvalidHttpRequestException {

        try {
            String line = input.readLine();
            if (line == null) {
                line = "";
            }
            return line;
        } catch (IOException e) {
            throw new InvalidHttpRequestException(e);
        }
    }


    private void parseHeaders(BufferedReader input) throws InvalidHttpRequestException {
        headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        String headerLine = readLine(input);
        while (!headerLine.isEmpty()) {
            //Parse header
            String[] headerElements = headerLine.split(":", 2);
            if (headerElements.length != 2 || headerElements[1].isEmpty()) {
                throw new InvalidHttpRequestException("Invalid header pair for headerLine \"" + headerLine+"\"");
            }
            //Creates
            String headerName = headerElements[0].trim();
            String headerValue = headerElements[1].trim();

            //gets the list of a header field, if it's nothing create arraylist
            List<String> values = List.of(headerValue);

            headers.put(headerName, values);
            headerLine = readLine(input);
        }


    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    private int getContentLength() throws InvalidHttpRequestException {

        List<String> contentLengthHeader = headers.get("content-length");
        if (contentLengthHeader == null) {
            return 0;
        }
        if(contentLengthHeader.isEmpty()){
            throw new InvalidHttpRequestException("No value in Content-length header");
        }
        try {
            return Integer.parseInt(contentLengthHeader.getFirst());

        } catch (NumberFormatException e) {

            throw new InvalidHttpRequestException("Invalid Content-Length value: "  + contentLengthHeader.getFirst(), e);
        }
    }
}
