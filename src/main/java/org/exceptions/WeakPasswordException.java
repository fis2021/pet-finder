package org.exceptions;

public class WeakPasswordException extends Exception {

    private static final long serialVersionUID = 1L;

    public WeakPasswordException() {
        super(String.format("Password must be at least 8 characters long and contain:\n1 uppercase letter\n1 lowercase letter\n1 number\n1 special char"));
    }
}
