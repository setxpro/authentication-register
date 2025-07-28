package br.com.makeconsultores.oauth_register.infra.persistences.dtos;

import br.com.makeconsultores.oauth_register.infra.persistences.User;
import br.com.makeconsultores.oauth_register.infra.persistences.enums.Role;

public record UserSystemDTO(
        Long id,
        String name,
        Role role
) {
    public static UserSystemDTO from(User user) {
        return new UserSystemDTO(
                user.getId(),
                user.getName(),
                user.getAccess().getRole()
        );
    }
}
