package br.com.makeconsultores.oauth_register.infra.persistences;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
