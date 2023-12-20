package no.lullinj.http;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpRequestParserTest {

    @Test
    void testParseHttpRequestShouldParseStatusLine() {
        String mockRequest= "GET / HTTP/1.1\r\nHost: host\r\nName: Amund\r\n\r\n";
        InputStream stream = new ByteArrayInputStream(mockRequest.getBytes());

        HttpRequestParser parser = new HttpRequestParser();
        try {
            HttpRequest request = parser.parseHttpRequest(stream);
            HttpMethod methode = request.getMethod();
            String uri = request.getUri();
            String version = request.getVersion();
            assertEquals(methode, HttpMethod.GET);
            assertEquals(uri, "/");
            assertEquals(version, "HTTP/1.1");
        } catch (InvalidHttpRequestException e) {
            fail("Exception should not be thrown.");
        }
    }
    @Test
    void testParseHttpRequestShouldParseHeaders() {
        String mockRequest= "GET / HTTP/1.1\r\nHost: host\r\nName: Amund\r\n\r\n";
        InputStream stream = new ByteArrayInputStream(mockRequest.getBytes());

        HttpRequestParser parser = new HttpRequestParser();
        try {
            HttpRequest request = parser.parseHttpRequest(stream);
            List<String> header= request.getHeader("name");
            assertEquals(List.of("Amund"), header);
        } catch (InvalidHttpRequestException e) {
            fail("Exception should not be thrown.");
        }
    }

    @Test
    void testParseHttpRequestShouldParseBody(){
        String mockRequest= "POST / HTTP/1.1\r\nHost: host\r\nContent-Length: 20\r\n\r\nHei, jeg heter amund";
        InputStream stream = new ByteArrayInputStream(mockRequest.getBytes());
        String expectedBody ="Hei, jeg heter amund";
        HttpRequestParser parser = new HttpRequestParser();
        try {
            HttpRequest request = parser.parseHttpRequest(stream);
            List<String> header= request.getHeader("content-length");
            String body = request.getBody();
            assertEquals(List.of("20"), header);
            assertEquals(body.length(), expectedBody.length());
            assertEquals(body, expectedBody);
        } catch (InvalidHttpRequestException e) {
            fail("Exception should not be thrown.");
        }
    }


    @Test
    void testParseHttpRequestGetShouldIgnoreBodyOnGet(){
        String mockRequest= "GET / HTTP/1.1\r\nHost: host\r\nContent-Length: 20\r\n\r\nHei, jeg heter amund";
        InputStream stream = new ByteArrayInputStream(mockRequest.getBytes());
        HttpRequestParser parser = new HttpRequestParser();
        try {
            HttpRequest request = parser.parseHttpRequest(stream);
            List<String> header= request.getHeader("content-length");
            String body = request.getBody();
            assertEquals(List.of("20"), header);
            assert(body.isEmpty());
        } catch (InvalidHttpRequestException e) {
            fail("Exception should not be thrown.");
        }
    }

    @Test
    void testParseHttpRequestGetShouldIgnoreBodyOnDelete(){
        String mockRequest= "DELETE / HTTP/1.1\r\nHost: host\r\nContent-Length: 20\r\n\r\nHei, jeg heter amund";
        InputStream stream = new ByteArrayInputStream(mockRequest.getBytes());
        HttpRequestParser parser = new HttpRequestParser();
        try {
            HttpRequest request = parser.parseHttpRequest(stream);
            List<String> header= request.getHeader("content-length");
            String body = request.getBody();
            assertEquals(List.of("20"), header);
            assert(body.isEmpty());
        } catch (InvalidHttpRequestException e) {
            fail("Exception should not be thrown.");
        }
    }
    @Test
    void testParseHttpRequestThrowsWhenBodyTooShort(){
        String mockRequest= "POST / HTTP/1.1\r\nHost: host\r\nContent-Length: 20\r\n\r\nHei, jeg heter ";
        InputStream stream = new ByteArrayInputStream(mockRequest.getBytes());
        HttpRequestParser parser = new HttpRequestParser();

        assertThrows(InvalidHttpRequestException.class, () -> parser.parseHttpRequest(stream));

    }

    @Test
    void testParserHttpRequestThrowsWhenInvalidStatusLine(){
        String mockRequest= "POST HTTP/1.1\r\nHost: host\r\nContent-Length: 20\r\n\r\nHei, jeg heter amund";
        InputStream stream = new ByteArrayInputStream(mockRequest.getBytes());
        HttpRequestParser parser = new HttpRequestParser();

        InputStream finalStream1 = stream;
        assertThrows(InvalidHttpRequestException.class, () -> parser.parseHttpRequest(finalStream1));

        mockRequest= "/ HTTP/1.1\r\nHost: host\r\nContent-Length: 20\r\n\r\nHei, jeg heter amund";
        stream = new ByteArrayInputStream(mockRequest.getBytes());


        InputStream finalStream2 = stream;
        assertThrows(InvalidHttpRequestException.class, () -> parser.parseHttpRequest(finalStream2));

        mockRequest= "POST / \r\nHost: host\r\nContent-Length: 20\r\n\r\nHei, jeg heter amund";
        stream = new ByteArrayInputStream(mockRequest.getBytes());
        InputStream finalStream3 = stream;
        assertThrows(InvalidHttpRequestException.class, () -> parser.parseHttpRequest(finalStream3));

        mockRequest= "POST / HTTP/1.1 P\r\nHost: host\r\nContent-Length: 20\r\n\r\nHei, jeg heter amund";
        stream = new ByteArrayInputStream(mockRequest.getBytes());
        InputStream finalStream4 = stream;
        assertThrows(InvalidHttpRequestException.class, () -> parser.parseHttpRequest(finalStream4));
    }

    @Test
    void testParseHttpRequestThrowsWhenContentLengthIsInvalid(){
        String mockRequest= "POST / HTTP/1.1\r\nHost: host\r\nContent-Length: a\r\n\r\nHei, jeg heter amund";
        InputStream stream = new ByteArrayInputStream(mockRequest.getBytes());
        HttpRequestParser parser = new HttpRequestParser();

        Exception exception = assertThrows(InvalidHttpRequestException.class, () -> parser.parseHttpRequest(stream));
        assertEquals("Invalid Content-Length value: a",exception.getMessage());
        assertEquals(NumberFormatException.class, exception.getCause().getClass());
    }


    @Test
    void testParseHttpRequestThrowsWhenHeaderIsMissingValue() throws InvalidHttpRequestException {
        String mockRequest= "GET / HTTP/1.1\r\nHost: host\r\nContent-Length:\r\n\r\nHei, jeg heter amund";
        InputStream stream = new ByteArrayInputStream(mockRequest.getBytes());
        HttpRequestParser parser = new HttpRequestParser();
        Exception exception = assertThrows(InvalidHttpRequestException.class, () -> parser.parseHttpRequest(stream));
        assertEquals("Invalid header pair for headerLine \"Content-Length:\"",exception.getMessage());
    }

    @Test
    void testParseHttpRequestThrowsWhenInvalidMethod(){
        String mockRequest= "GETT / HTTP/1.1\r\nHost: host\r\nContent-Length: 20\r\n\r\nHei, jeg heter amund";
        InputStream stream = new ByteArrayInputStream(mockRequest.getBytes());
        HttpRequestParser parser = new HttpRequestParser();
        Exception exception = assertThrows(InvalidHttpRequestException.class, () -> parser.parseHttpRequest(stream));
        assertEquals("Unexpected value for method \"GETT\"",exception.getMessage());
    }

//Should headers be split? not sure need to do research
//    @Test
//    void testParseHttpRequestParseListHeaders() {
//        String mockRequest= "GET / HTTP/1.1\r\nHost: host\r\nnames: amund;fredrik;brede\r\n\r\n";
//        InputStream stream = new ByteArrayInputStream(mockRequest.getBytes());
//
//        HttpRequestParser parser = new HttpRequestParser();
//        try {
//            HttpRequest request = parser.parseHttpRequest(stream);
//            List<String> header= request.getHeader("names");
//            assertEquals(List.of("amund", "brede","fredrik"), header);
//        } catch (InvalidHttpRequestException e) {
//            fail("Exception should not be thrown.");
//        }
//    }

}