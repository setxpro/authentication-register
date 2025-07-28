package br.com.makeconsultores.oauth_register.infra.persistences;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface AccessRepository extends JpaRepository<Access, Long> {
    UserDetails findByUsername(String username);
    UserDetails findAccessById(Long id);

    @Query(value = "SELECT * FROM tb_access WHERE username = :username", nativeQuery = true)
    Optional<Access> findAccQuery(@Param("username") String username);
}
