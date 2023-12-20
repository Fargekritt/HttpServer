package no.lullinj.http;

import no.lullinj.InvalidHttpRequestException;
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
            String methode = request.getMethod();
            String uri = request.getUri();
            String version = request.getVersion();
            assertEquals(methode, "GET");
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
    void testParseHttpRequestShouldParseBdy(){
        String mockRequest= "GET / HTTP/1.1\r\nHost: host\r\nContent-Length: 20\r\n\r\nHei, jeg heter amund";
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
    void testParseHttpRequestShouldThrowWhenBodyTooShort(){
        String mockRequest= "GET / HTTP/1.1\r\nHost: host\r\nContent-Length: 20\r\n\r\nHei, jeg heter ";
        InputStream stream = new ByteArrayInputStream(mockRequest.getBytes());
        HttpRequestParser parser = new HttpRequestParser();

        assertThrows(InvalidHttpRequestException.class, () -> parser.parseHttpRequest(stream));


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