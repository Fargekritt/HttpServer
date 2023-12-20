package no.lullinj.http;

import no.lullinj.InvalidHttpRequestException;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HttpRequestParser {

    private Map<String, List<String>> headers;
    private String body;
    private String method;
    private String uri;
    private String version;


    public HttpRequest parseHttpRequest(InputStream inputStream) throws InvalidHttpRequestException {
        BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));

        parseStatusLine(input);
        parseHeaders(input);
        parseBody(input);


        return new HttpRequest(headers, body, method, uri, version);
    }

    private void parseBody(BufferedReader input) {
        if (getContentLength() < 1) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        try {
            int contentLength = getContentLength();
            char[] buffer = new char[contentLength];
            int _ = input.read(buffer);
            stringBuilder.append(buffer);
            body = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void parseStatusLine(BufferedReader input) throws InvalidHttpRequestException {
        String[] statusLine = readLine(input).split(" ");
        if (statusLine.length != 3) {
            throw new InvalidHttpRequestException();
        }

        method = statusLine[0];
        uri = statusLine[1];
        version = statusLine[2];
    }


    private String readLine(BufferedReader input) {

        try {
            String line = input.readLine();
            if (line == null) {
                line = "";
            }
            return line;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


    private void parseHeaders(BufferedReader input) throws InvalidHttpRequestException {
        headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        String headerLine = readLine(input);
        while (!headerLine.isEmpty()) {
            //Parse header
            String[] headerElements = headerLine.split(":", 2);
            if (headerElements.length != 2) {
                throw new InvalidHttpRequestException();
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

    private void parsBody(BufferedReader input) {

    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    private int getContentLength() {

        List<String> contentLengthHeader = headers.get("content-length");
        if (contentLengthHeader == null || contentLengthHeader.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(contentLengthHeader.getFirst());

        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
