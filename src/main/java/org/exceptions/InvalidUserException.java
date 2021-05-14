package org.exceptions;

public class InvalidUserException extends Exception {
    public InvalidUserException() {
        super("Invalid username/password");
    }
}
