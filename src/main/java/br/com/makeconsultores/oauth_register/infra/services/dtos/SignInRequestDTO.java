package br.com.makeconsultores.oauth_register.infra.services.dtos;

public record SignInRequestDTO(
    String username,
    String password
) {
}
