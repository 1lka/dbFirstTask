package com.dbbest.kirilenko.exceptions;

public class SerializationExeption extends Exception {
    public SerializationExeption() {
    }

    public SerializationExeption(String message) {
        super(message);
    }

    public SerializationExeption(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializationExeption(Throwable cause) {
        super(cause);
    }

    public SerializationExeption(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
