package br.com.makeconsultores.oauth_register.infra.services;

import br.com.makeconsultores.oauth_register.delivery.exceptions.NotFoundException;
import br.com.makeconsultores.oauth_register.infra.persistences.dtos.SystemDTO;
import br.com.makeconsultores.oauth_register.infra.persistences.system.SystemAccess;
import br.com.makeconsultores.oauth_register.infra.persistences.system.SystemAccessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemAccessService {

    @Autowired
    private SystemAccessRepository repository;

    public void saveSystem(SystemAccess req) {
        repository.save(req);
    }

    public List<SystemDTO> findAllSystems() {
        return repository.findAll()
                .stream()
                .filter(SystemAccess::getActive)
                .map(SystemDTO::from)
                .toList();
    }

    public SystemAccess findOneSystem(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Sistema não encontrado."));
    }

    public void deleteOneSystem(Long id) {
        repository.findById(id).orElseThrow(() -> new NotFoundException("Systema não encontrado."));
        repository.deleteById(id);
    }

}
