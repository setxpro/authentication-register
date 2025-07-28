package br.com.makeconsultores.oauth_register.infra.services.dtos;

public record MessageForgetDTO(
        String message,
        int code,
        String id
) {
}
