package ru.cian.test.api;

public class ServerApiException extends Exception {
    public ServerApiException() {
        super();
    }

    public ServerApiException(String message) {
        super(message);
    }

    public ServerApiException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ServerApiException(Throwable throwable) {
        super(throwable);
    }
}