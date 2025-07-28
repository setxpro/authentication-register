package br.com.makeconsultores.oauth_register.infra.persistences.dtos;

import br.com.makeconsultores.oauth_register.infra.persistences.Access;
import br.com.makeconsultores.oauth_register.infra.persistences.User;
import br.com.makeconsultores.oauth_register.infra.persistences.enums.Role;
import br.com.makeconsultores.oauth_register.infra.persistences.system.SystemAccess;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public record UserSignInDTO(
        Long userId,
        Long accessId,
        String name,
        String email,
        String phone,
        String username,
        String password,
        Role role,
        Boolean active,
        Boolean accountNonLocked,
        Boolean accountNonExpired,
        Boolean credentialsNonExpired,
        Boolean enabled,
        List<String> authorities,
        String token,
        List<SystemAccess> systems
) {
    public static UserSignInDTO toDto(User user, Access access, UserDetails userDetails, String token) {
        return new UserSignInDTO(
                user.getId(),
                access.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                access.getUsername(),
                access.getPassword(),
                access.getRole(),
                access.getActive(),
                userDetails.isAccountNonLocked(),
                userDetails.isCredentialsNonExpired(),
                userDetails.isCredentialsNonExpired(),
                userDetails.isEnabled(),
                userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(),
                token,
                user.getSystems()
        );
    }
}