package br.com.makeconsultores.oauth_register.infra.persistences.dtos;

import br.com.makeconsultores.oauth_register.infra.persistences.Authority;
import br.com.makeconsultores.oauth_register.infra.persistences.enums.Role;

import java.util.List;


public record UserDTO(
        String name,
        String email,
        String phone,
        Role role,
        String username,
        List<Authority> authorities
) {
}
