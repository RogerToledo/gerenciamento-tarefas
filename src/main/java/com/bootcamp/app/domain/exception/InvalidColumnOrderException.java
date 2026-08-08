package com.bootcamp.app.domain.exception;

public class InvalidColumnOrderException extends DomainException {
    public InvalidColumnOrderException(String message) {
        super(message);
    }
}
