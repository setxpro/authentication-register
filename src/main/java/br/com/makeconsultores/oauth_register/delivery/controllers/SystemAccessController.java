package br.com.makeconsultores.oauth_register.delivery.controllers;

import br.com.makeconsultores.oauth_register.infra.persistences.dtos.SystemDTO;
import br.com.makeconsultores.oauth_register.infra.persistences.system.SystemAccess;
import br.com.makeconsultores.oauth_register.infra.services.SystemAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static br.com.makeconsultores.oauth_register.infra.config.SecurityConfig.SYSTEM_PATH;

@RestController
@RequestMapping(SYSTEM_PATH)
public class SystemAccessController {

    @Autowired
    private SystemAccessService service;

    @PostMapping
    public ResponseEntity<?> saveSystem(@RequestBody SystemAccess req) {
        service.saveSystem(req);
        return new ResponseEntity<>("", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SystemDTO>> findAllSystems() {
        return new ResponseEntity<>(service.findAllSystems(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SystemAccess> findOneSystem(@PathVariable Long id) {
        return new ResponseEntity<>(service.findOneSystem(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOneSystem(@PathVariable Long id) {
        service.deleteOneSystem(id);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

}
