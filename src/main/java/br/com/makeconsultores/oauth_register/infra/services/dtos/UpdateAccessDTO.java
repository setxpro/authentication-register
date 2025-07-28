package br.com.makeconsultores.oauth_register.infra.services.dtos;

import br.com.makeconsultores.oauth_register.infra.persistences.enums.Role;

public record UpdateAccessDTO(
       String username,
       Role role,
       Boolean active
) {
}
