package no.lullinj;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello world!");
        Server server = new Server(8080);
        server.start();
    }
}