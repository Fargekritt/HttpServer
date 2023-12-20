package no.lullinj.http;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class is Thread-safe. It can safely be used to parse
 * HTTP requests concurrently in multiple threads
 */
public class HttpRequestParser {
    private record ParsedStatusLine(HttpMethod method, String uri, String version){

    }


    public HttpRequest parseHttpRequest(InputStream inputStream) throws InvalidHttpRequestException {
        BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));

        Map<String, List<String>> headers;
        int contentLength;
        String body;
        ParsedStatusLine statusLine;
        statusLine = parseStatusLine(input);
        headers = parseHeaders(input);
        contentLength = getContentLength(headers);
        body = parseBody(input, contentLength, statusLine.method);

        try {
            input.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new HttpRequest(headers, body, statusLine.method, statusLine.uri, statusLine.version);
    }

    private String parseBody(BufferedReader input, int contentLength, HttpMethod method) throws InvalidHttpRequestException {
        if (contentLength < 1 || method.equals(HttpMethod.GET) || method.equals(HttpMethod.DELETE)) {

            return "";
        }

        String body;
        try {
            char[] buffer = new char[contentLength];
            int readBytesLength = input.read(buffer);
            if (readBytesLength < contentLength) {
                throw new InvalidHttpRequestException("Body too short, Content-Length:" + contentLength + " Body Length: " + readBytesLength);
            }
            body = new String(buffer);
        } catch (IOException e) {
            throw new InvalidHttpRequestException(e);
        }


        return body;
    }

    private ParsedStatusLine parseStatusLine(BufferedReader input) throws InvalidHttpRequestException {
        String[] statusLine = readLine(input).split(" ");
        if (statusLine.length != 3) {
            throw new InvalidHttpRequestException("Invalid Status line: " + Arrays.toString(statusLine));
        }

        try {

            HttpMethod method = HttpMethod.valueOf(statusLine[0]);
            String uri = statusLine[1];
            String version = statusLine[2];
            return new ParsedStatusLine(method, uri, version);
        } catch (IllegalArgumentException e) {
            throw new InvalidHttpRequestException("Unexpected value for method \"" + statusLine[0] + "\"");
        }
    }


    private String readLine(BufferedReader input) throws InvalidHttpRequestException {

        try {
            String line = input.readLine();
            if (line == null) {
                return "";
            }
            return line;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private Map<String, List<String>> parseHeaders(BufferedReader input) throws InvalidHttpRequestException {
        Map<String, List<String>> headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        String headerLine = readLine(input);
        while (!headerLine.isEmpty()) {
            //Parse header
            String[] headerElements = headerLine.split(":", 2);
            if (headerElements.length != 2 || headerElements[1].isEmpty()) {
                throw new InvalidHttpRequestException("Invalid header pair for headerLine \"" + headerLine + "\"");
            }
            //Creates
            String headerName = headerElements[0].trim().toLowerCase();
            String headerValue = headerElements[1].trim();


            //gets the list of a header field, if it's nothing create arraylist
            List<String> values = List.of(headerValue);

            headers.put(headerName, values);
            headerLine = readLine(input);
        }

        return headers;
    }


    private int getContentLength(Map<String, List<String>> headers) throws InvalidHttpRequestException {
        List<String> contentLengthHeader = headers.get("content-length");
        if (contentLengthHeader == null) {
            return 0;
        }
        if (contentLengthHeader.isEmpty()) {
            throw new InvalidHttpRequestException("No value in Content-length header");
        }
        try {
            return Integer.parseInt(contentLengthHeader.getFirst());

        } catch (NumberFormatException e) {

            throw new InvalidHttpRequestException("Invalid Content-Length value: " + contentLengthHeader.getFirst(), e);
        }
    }

}

