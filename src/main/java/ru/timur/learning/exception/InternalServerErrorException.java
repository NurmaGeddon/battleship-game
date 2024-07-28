package ru.timur.learning.exception;

public class InternalServerErrorException extends RuntimeException {

    public InternalServerErrorException(String message) {
        super(message);
    }
    public InternalServerErrorException(Exception exception) {
        super(exception);
    }
    public InternalServerErrorException(String message, Exception e) {
        super(message, e);
    }
    public InternalServerErrorException() {}
}
