package br.com.makeconsultores.oauth_register.delivery.exceptions;

public class FieldCannotEmptyException extends RuntimeException {
    public FieldCannotEmptyException(String message) {
        super(message);
    }
}
