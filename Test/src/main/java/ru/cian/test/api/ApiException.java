package ru.cian.test.api;

public class ApiException extends Exception {
    public ApiException() {
        super();
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ApiException(Throwable throwable) {
        super(throwable);
    }
}