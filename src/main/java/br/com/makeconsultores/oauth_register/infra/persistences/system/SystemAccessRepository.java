package br.com.makeconsultores.oauth_register.infra.persistences.system;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemAccessRepository extends JpaRepository<SystemAccess, Long> {
}
