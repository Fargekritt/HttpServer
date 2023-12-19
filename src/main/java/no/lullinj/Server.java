package no.lullinj;

import no.lullinj.http.HttpRequest;
import no.lullinj.http.HttpRequestParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;

/**
 * The Server class represents a server that listens for client connections on a specific port and responds with a simple HTTP response.
 */
public class Server {
    private final int port;
    private ServerSocket serverSocket;

    public Server(int port) {
        this.port = port;
    }


    /**
     * Starts the server, waits for a client connection, and sends a simple HTTP response.
     *
     * @throws IOException if an I/O error occurs
     */
    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        Socket clientSocket = serverSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        HttpRequest message = null;
        try {
            message = parseHttpRequest(clientSocket);
        } catch (InvalidHttpRequestException e) {
            throw new RuntimeException(e);
        }
        System.out.println(message.getHeader("content-length"));
        System.out.println(message.getBody());

        out.println("""
                HTTP/1.1 200 OK
                Content-length:4

                hei
                                
                """);
        System.out.println("Test");
        clientSocket.close();
    }


    private HttpRequest parseHttpRequest(Socket socket) throws InvalidHttpRequestException{
        HttpRequestParser parser = new HttpRequestParser();

        try {
            return parser.parseHttpRequest(socket.getInputStream());
        } catch (IOException e) {
            throw new InvalidHttpRequestException();
        }
    }



}
