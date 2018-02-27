package com.dbbest.kirilenko.exceptions;

/**
 * Base for Exceptions thrown during working with Nodes.
 */
public class NodeException extends RuntimeException {

    public NodeException() {
        super();
    }

    public NodeException(String message) {
        super(message);
    }
}
