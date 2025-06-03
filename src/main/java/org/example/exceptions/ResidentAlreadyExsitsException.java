package org.example.exceptions;

public class ResidentAlreadyExsitsException extends GatePassException{
    public ResidentAlreadyExsitsException(String message) {
        super(message);
    }
}
