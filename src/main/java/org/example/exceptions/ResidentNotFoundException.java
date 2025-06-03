package org.example.exceptions;

public class ResidentNotFoundException extends GatePassException{
    public ResidentNotFoundException(String message) {
        super(message);
    }
}
