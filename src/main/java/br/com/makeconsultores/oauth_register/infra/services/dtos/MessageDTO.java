package br.com.makeconsultores.oauth_register.infra.services.dtos;

public record MessageDTO(
        String message,
        int status
) {
}