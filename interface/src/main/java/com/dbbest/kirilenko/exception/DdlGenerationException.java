package com.dbbest.kirilenko.exception;

public class DdlGenerationException extends Exception {
    public DdlGenerationException() {
    }

    public DdlGenerationException(String message) {
        super(message);
    }

    public DdlGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DdlGenerationException(Throwable cause) {
        super(cause);
    }
}
