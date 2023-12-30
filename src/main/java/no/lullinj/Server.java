package no.lullinj;

import no.lullinj.http.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

/**
 * The Server class represents a server that listens for client connections on a specific port and responds with a simple HTTP response.
 */
public class Server {
    private final int port;

    private final File resources;

    public Server(int port, String folderName) {
        this.port = port;
        URL path = Main.class.getClassLoader().getResource(folderName);
        assert path != null;
        try {
            resources = new File(path.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
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
        HttpResponse response;
        HttpResponseBuilder builder = new HttpResponseBuilder();
        HttpRequest message;
        try {
            message = parseHttpRequest(clientSocket);


            String uri = message.getUri();
            File html = new File(resources, uri);

            if (html.isDirectory()) {
                html = new File(html, "index.html");
            }

            if (html.exists() && html.isFile()) {
                builder.setStatusCode(200);
            } else {
                html = new File(resources, "404.html");
                builder.setStatusCode(404);
            }


            StringBuilder body = new StringBuilder();
            FileReader reader = new FileReader(html);
            int c;
            while ((c = reader.read()) != -1) {
                char ch = (char) c;
                System.out.print(ch);
                body.append(ch);
            }
            builder.setBody(body.toString());
            reader.close();


        } catch (InvalidHttpRequestException e) {
            builder.setStatusCode(400);
        }
        response = builder.build();

        out.write(response.getStatusLine() + "\n" + "Content-Length:" + response.getBody().length + "\n\n");
        out.flush();
        out.write(new String(response.getBody()));
        out.flush();


        System.out.println("Test");
        clientSocket.close();
    }


    private HttpRequest parseHttpRequest(Socket socket) throws InvalidHttpRequestException {
        HttpRequestParser parser = new HttpRequestParser();

        try {
            return parser.parseHttpRequest(socket.getInputStream());
        } catch (IOException e) {
            throw new InvalidHttpRequestException();
        }
    }


}
