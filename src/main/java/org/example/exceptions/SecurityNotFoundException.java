package org.example.exceptions;

public class SecurityNotFoundException extends GatePassException{
    public SecurityNotFoundException(String message) {
        super(message);
    }

}
