package org.exceptions;

public class UserDoesNotExistException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String username;

    public UserDoesNotExistException(String username) {
        super(String.format("There is no account linked to the username: %s", username));
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
