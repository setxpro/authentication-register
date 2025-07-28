package br.com.makeconsultores.oauth_register.infra.services.dtos;

public record UpdateAccessWhenForgetPassword(
        String newPassword,
        String confirmPassword
) {
}
