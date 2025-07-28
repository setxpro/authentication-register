package br.com.makeconsultores.oauth_register.delivery.exceptions;

public class BadValidationException extends RuntimeException {
    public BadValidationException(String message) {
        super(message);
    }
}
