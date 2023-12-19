package no.lullinj.http;

import no.lullinj.InvalidHttpRequestException;

import java.io.*;
import java.util.ArrayList;
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
        String[] statusLine = readLine(input).split(" ");
        if (statusLine.length != 3) {
            throw new InvalidHttpRequestException();
        }

        method = statusLine[0];
        uri = statusLine[1];
        version = statusLine[2];

        readHeaders(input);


        return new HttpRequest(headers, body, method, uri);
    }


    private String readLine(BufferedReader input) {

        try {
            String line = input.readLine();
            if (line == null){
                line = "";
            }
            return line;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


    private void readHeaders(BufferedReader input) throws InvalidHttpRequestException {
        headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        String headerLine = readLine(input);
        String[] headerElements = headerLine.split(":", 2);
        if (headerElements.length != 2) {
            throw new InvalidHttpRequestException();
        }
        //Creates
        String headerName = headerElements[0].trim();
        String headerValue = headerElements[1].trim();

        //gets the list of a header field, if it's nothing create arraylist
        List<String> values = List.of(headerValue.split(";"));

        headers.put(headerName, values);

    }
}
