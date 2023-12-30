package no.lullinj;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        System.out.println("Hello world!");
        System.out.println();

        Server server = new Server(9900, "static");
        server.start();

    }

}