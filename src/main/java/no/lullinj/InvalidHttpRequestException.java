package no.lullinj;

import java.io.IOException;

public class InvalidHttpRequestException extends IOException {
    public InvalidHttpRequestException() {
    }

    public InvalidHttpRequestException(String message) {
        super(message);
    }

    public InvalidHttpRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidHttpRequestException(Throwable cause) {
        super(cause);
    }
}
