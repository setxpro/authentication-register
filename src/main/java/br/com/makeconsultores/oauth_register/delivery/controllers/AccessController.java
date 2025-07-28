package br.com.makeconsultores.oauth_register.delivery.controllers;

import br.com.makeconsultores.oauth_register.infra.persistences.Access;
import br.com.makeconsultores.oauth_register.infra.services.AccessService;
import br.com.makeconsultores.oauth_register.infra.services.dtos.MessageDTO;
import br.com.makeconsultores.oauth_register.infra.services.dtos.UpdateAccessDTO;
import br.com.makeconsultores.oauth_register.infra.services.dtos.UpdateAccessWhenForgetPassword;
import br.com.makeconsultores.oauth_register.infra.services.dtos.UpdatePasswordDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static br.com.makeconsultores.oauth_register.infra.config.SecurityConfig.ACCESS_PATH;

@RestController
@RequestMapping(ACCESS_PATH)
public class AccessController {

    private final AccessService accessService;

    public AccessController(AccessService accessService) {
        this.accessService = accessService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Access> findOneAccess(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(accessService.findOneAccess(id), HttpStatus.OK);
    }

    @PutMapping("/update-access/{id}")
    public ResponseEntity<MessageDTO> updateOneAccess(@PathVariable(value = "id") Long id, @RequestBody UpdateAccessDTO req) {
        return new ResponseEntity<>(accessService.updateOneAccess(id, req), HttpStatus.OK);
    }

    @PutMapping("/update-password/{id}")
    public ResponseEntity<MessageDTO> updateOnePassword(@PathVariable(value = "id") Long id, @RequestBody UpdatePasswordDTO req) {
        return new ResponseEntity<>(accessService.updatePassword(id, req), HttpStatus.OK);
    }

    @PutMapping("/password-forgot/{id}")
    public ResponseEntity<MessageDTO> updatePassWhenForget(@PathVariable(value = "id") Long id, @RequestBody UpdateAccessWhenForgetPassword req) {
        return new ResponseEntity<>(accessService.updatePasswordWhenForgetPass(id, req), HttpStatus.OK);
    }
}
