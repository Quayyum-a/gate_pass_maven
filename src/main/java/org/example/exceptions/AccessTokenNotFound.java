package org.example.exceptions;

public class AccessTokenNotFound extends GatePassException{
    public AccessTokenNotFound(String message) {
        super(message);
    }
}
