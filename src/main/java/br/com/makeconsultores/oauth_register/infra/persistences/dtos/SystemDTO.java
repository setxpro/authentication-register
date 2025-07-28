package br.com.makeconsultores.oauth_register.infra.persistences.dtos;

import br.com.makeconsultores.oauth_register.infra.persistences.User;
import br.com.makeconsultores.oauth_register.infra.persistences.system.SystemAccess;

import java.util.List;

public record SystemDTO(
        Long id,
        String name,
        String url,
        Boolean active,
        List<UserSystemDTO> users
) {
    public static SystemDTO from(SystemAccess system) {
        List<UserSystemDTO> userDTOs = system.getUsers().stream()
                .map(UserSystemDTO::from)
                .toList();

        return new SystemDTO(
                system.getId(),
                system.getName(),
                system.getUrl(),
                system.getActive(),
                userDTOs
        );
    }
}
