package br.com.makeconsultores.oauth_register.infra.services.dtos;

public record RequestCode(
        String userId,
        String email,
        String name
) {
}
