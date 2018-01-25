package com.dbbest.kirilenko.exceptions;

public class ArgsInputException extends RuntimeException {

    public ArgsInputException(String message) {
        super(message);
    }

    public ArgsInputException(String message, Throwable cause) {
        super(message, cause);

    }

}
