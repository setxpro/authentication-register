package br.com.makeconsultores.oauth_register.infra.services.dtos;

public record UpdatePasswordDTO(
        String oldPassword,
        String newPassword,
        String confirmPassword
) {
}
