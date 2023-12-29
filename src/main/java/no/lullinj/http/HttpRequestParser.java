package no.lullinj.http;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.jetbrains.annotations.Contract;


/**
 * Stateless class
 * This class is Thread-safe. It can safely be used to parse
 * HTTP requests concurrently in multiple threads
 */
public class HttpRequestParser {
    private record ParsedStatusLine(HttpMethod method, String uri, String version) {

    }


    /**
     * Parses an HTTP request from the given input stream.
     *
     * @param inputStream the input stream to read the request from
     * @return the parsed HTTP request
     * @throws InvalidHttpRequestException if the HTTP request is invalid
     */
    @Contract(pure=true)
    public HttpRequest parseHttpRequest(InputStream inputStream) throws InvalidHttpRequestException {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII))) {

            //Local Variables, keeps the class stateless
            Map<String, List<String>> headers;
            int contentLength;
            char[] body;
            ParsedStatusLine statusLine;
            List<Cookie> cookies;

            //Parse the request
            statusLine = parseStatusLine(input);
            headers = parseHeaders(input);
            contentLength = getContentLength(headers);
            body = parseBody(input, contentLength, statusLine.method);
            cookies = parseCookie(headers);

            //Create the request
            return new HttpRequest(headers, body, statusLine.method, statusLine.uri, statusLine.version, cookies);
        } catch (InvalidHttpRequestException e) {
            throw e;
        } catch (IOException e) {
            throw new InvalidHttpRequestException(e);
        }

    }


    private char[] parseBody(BufferedReader input, int contentLength, HttpMethod method) throws InvalidHttpRequestException {
        if (contentLength < 1 || method.equals(HttpMethod.GET) || method.equals(HttpMethod.DELETE)) {
            return new char[0];
        }

        char[] buffer;
        try {
            // TODO:
            //  Chunk data for large sizes
            buffer = new char[contentLength];
            int readBytesLength = input.read(buffer);
            if (readBytesLength < contentLength) {
                throw new InvalidHttpRequestException("Body too short, Content-Length:" + contentLength + " Body Length: " + readBytesLength);
            }
        } catch (IOException e) {
            throw new InvalidHttpRequestException(e);
        }
        return buffer;
    }

    private ParsedStatusLine parseStatusLine(BufferedReader input) throws InvalidHttpRequestException {
        try {
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
        } catch (EOFException e) {
            throw new InvalidHttpRequestException("Unexpected EOF When parsing status line ", e);
        }
    }



    private String readLine(BufferedReader input) throws EOFException {

        try {
            String line = input.readLine();
            if (line == null) {
                throw new EOFException();
            }
            return line;
        } catch (EOFException e) {
            throw e;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    private Map<String, List<String>> parseHeaders(BufferedReader input) throws InvalidHttpRequestException {
        Map<String, List<String>> headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        String headerLine;
        try {
            headerLine = readLine(input);
            while (!headerLine.isEmpty()) {
                //Parse header
                String[] headerElements = headerLine.split(":", 2);
                if (headerElements.length != 2 || headerElements[1].isEmpty()) {
                    throw new InvalidHttpRequestException("Invalid header pair for headerLine \"" + headerLine + "\"");
                }
                // Cleans up the header name of value
                String headerName = headerElements[0].trim().toLowerCase();
                String headerValue = headerElements[1].trim();


                List<String> values = new ArrayList<>();
                // If headerField is a cookie it should not be seperated on ","
                // According to RFC7230#section-3.2.2
                if(!headerName.equals("cookie")){
                    String[] headerValues = headerValue.split(",");
                    values.addAll(Arrays.asList(headerValues));
                } else {
                    values.add(headerValue);
                }


                //gets the list of a header field, if it's nothing create arraylist
                if(headers.containsKey(headerName)){
                    headers.get(headerName).addAll(values);
                } else {
                    headers.put(headerName, values);
                }
                headerLine = readLine(input);
            }
        } catch (EOFException e) {
            throw new InvalidHttpRequestException("Unexpected EOF while parsing header ", e);
        }

        return headers;
    }


    private int getContentLength(Map<String, List<String>> headers) throws InvalidHttpRequestException {
        List<String> contentLengthHeader = headers.get("content-length");
        if (contentLengthHeader == null) {
            return 0;
        }

        //Should never happen, header parser should throw before this can happen.
        if (contentLengthHeader.isEmpty()) {
            throw new InvalidHttpRequestException("No value in Content-length header");
        }
        try {
            return Integer.parseInt(contentLengthHeader.getFirst());
        } catch (NumberFormatException e) {
            throw new InvalidHttpRequestException("Invalid Content-Length value: " + contentLengthHeader.getFirst(), e);
        }
    }

    private List<Cookie> parseCookie(Map<String, List<String>> headers){
        List<String> cookieStrings = headers.get("cookie");
        List<Cookie> cookies = new ArrayList<>();

        if(cookieStrings == null){
            return cookies;
        }
        for (String cookieString : cookieStrings) {
            Cookie cookie;
            String[] elements = cookieString.split(";");
            if(elements.length == 1){
                String[] nameValue = elements[0].split("=");
                cookie = new Cookie(nameValue[0], nameValue[1]);
                cookies.add(cookie);
            }
        }


        return cookies;
    }
}

