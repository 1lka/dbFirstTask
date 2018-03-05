package com.dbbest.kirilenko.exceptions;

public class LoadingException extends RuntimeException {
    public LoadingException() {
    }

    public LoadingException(String message) {
        super(message);
    }

    public LoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
