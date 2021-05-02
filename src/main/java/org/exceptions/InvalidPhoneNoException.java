package org.exceptions;

public class InvalidPhoneNoException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidPhoneNoException() {
        super(String.format("Invalid phone number"));
    }
}