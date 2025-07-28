package br.com.makeconsultores.oauth_register.infra.services.dtos;

public record ValidateCodeResponseDTO(
        String message,
        Boolean status,
        Long id
) {
}
