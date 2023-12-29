package no.lullinj;

import no.lullinj.http.HttpRequest;
import no.lullinj.http.HttpRequestParser;
import no.lullinj.http.InvalidHttpRequestException;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The Server class represents a server that listens for client connections on a specific port and responds with a simple HTTP response.
 */
public class Server {
    private final int port;

    public Server(int port) {
        this.port = port;
    }


    /**
     * Starts the server, waits for a client connection, and sends a simple HTTP response.
     *
     * @throws IOException if an I/O error occurs
     */
    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        Socket clientSocket = serverSocket.accept();
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        HttpRequest message;
        try {
            message = parseHttpRequest(clientSocket);
        } catch (InvalidHttpRequestException e) {
            throw new RuntimeException(e);
        }
        System.out.println(message.getHeader("content-length"));


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
