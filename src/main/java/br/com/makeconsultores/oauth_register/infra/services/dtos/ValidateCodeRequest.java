package br.com.makeconsultores.oauth_register.infra.services.dtos;

public record ValidateCodeRequest(
        String hash,
        String userId
) {
}
