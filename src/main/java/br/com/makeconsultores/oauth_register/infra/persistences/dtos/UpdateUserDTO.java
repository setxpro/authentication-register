package br.com.makeconsultores.oauth_register.infra.persistences.dtos;

public record UpdateUserDTO(
        String name,
        String email,
        String phone
) {
}
