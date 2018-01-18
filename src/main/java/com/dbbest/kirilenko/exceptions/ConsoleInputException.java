package com.dbbest.kirilenko.exceptions;

public class ConsoleInputException extends RuntimeException {

    public ConsoleInputException(String message) {
        super(message);
    }

    public ConsoleInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
