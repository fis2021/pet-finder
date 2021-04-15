package org.exceptions;

public class InvalidUserException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public InvalidUserException() {
        super(String.format("Invalid username/password"));
    }
}
