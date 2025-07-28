package br.com.makeconsultores.oauth_register.delivery.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
