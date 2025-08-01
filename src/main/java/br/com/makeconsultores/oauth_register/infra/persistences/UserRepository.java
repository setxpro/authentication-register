package br.com.makeconsultores.oauth_register.infra.persistences;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByAccess_Id(Long accessId);
}
