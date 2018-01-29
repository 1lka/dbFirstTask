package com.dbbest.kirilenko.exceptions;

/**
 * Base for Exceptions thrown during parsing of a command-line.
 */
public class ArgsInputException extends RuntimeException {

    public ArgsInputException(String message) {
        super(message);
    }

    public ArgsInputException(String message, Throwable cause) {
        super(message, cause);

    }

}
