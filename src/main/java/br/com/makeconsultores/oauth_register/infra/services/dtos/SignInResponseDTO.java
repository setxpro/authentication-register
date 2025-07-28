package br.com.makeconsultores.oauth_register.infra.services.dtos;

import org.springframework.security.core.userdetails.UserDetails;

public record SignInResponseDTO(
        UserDetails user,
        String token
) {
}
