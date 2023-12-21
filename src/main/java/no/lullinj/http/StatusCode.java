package no.lullinj.http;

import java.util.HashMap;
import java.util.Map;

public class StatusCode {
    private static final Map<Integer, String> statusCodes = new HashMap<>();
    static {
        statusCodes.put(200, "OK");
        statusCodes.put(300, "MULTIPLE CHOICES");
        statusCodes.put(400, "BAD REQUEST");
        statusCodes.put(500, "INTERNAL SERVER ERROR");
    }


    public static void addStatusCode(int code, String message) {
        statusCodes.put(code, message);
    }

    public static String getStatusCodeMessage(int code) {
        return statusCodes.get(code);
    }
}
