package org.exceptions;

public class RequestAlreadyExistsException extends Exception{

    private static final long serialVersionUID = 1L;

    public RequestAlreadyExistsException() {
        super(String.format("You already sent a request to this user or you have sent a request to this announcement at least 3 times"));
    }
}
