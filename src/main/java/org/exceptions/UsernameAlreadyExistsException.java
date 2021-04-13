package org.exceptions;

public class UsernameAlreadyExistsException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String username;

    public UsernameAlreadyExistsException(String username) {
        super(String.format("An account with the username %s already exists!", username));
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
