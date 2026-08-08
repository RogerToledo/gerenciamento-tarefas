package com.bootcamp.app.domain.exception;

public class InvalidCardMovementException extends DomainException {
    public InvalidCardMovementException(String message) {
        super(message);
    }
}
