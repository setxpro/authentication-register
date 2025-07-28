package br.com.makeconsultores.oauth_register.delivery.controllers;

import br.com.makeconsultores.oauth_register.infra.persistences.Access;
import br.com.makeconsultores.oauth_register.infra.persistences.dtos.UserSignInDTO;
import br.com.makeconsultores.oauth_register.infra.services.AccessService;
import br.com.makeconsultores.oauth_register.infra.services.AuthService;
import br.com.makeconsultores.oauth_register.infra.services.dtos.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.DELETE;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static br.com.makeconsultores.oauth_register.infra.config.SecurityConfig.AUTH_PATH;

@RestController
@RequestMapping(AUTH_PATH)
public class AuthController {

    private final AuthService authService;
    private final AccessService accessService;

    public AuthController(AuthService authService, AccessService accessService) {
        this.authService = authService;
        this.accessService = accessService;
    }

    @PostMapping("/sign-in")
    private ResponseEntity<UserSignInDTO> signIn(@RequestBody SignInRequestDTO req, HttpServletResponse resp) {
        return new ResponseEntity<>(authService.signin(req, resp), HttpStatus.OK);
    }

    @PostMapping("/forget-password")
    public ResponseEntity<MessageForgetDTO> forgetPassword(@RequestBody ForgetPasswordRequest req) {
        return new ResponseEntity<>(authService.forgetPassword(req), HttpStatus.OK);
    }

    @PostMapping("/validate-code")
    public ResponseEntity<ValidateCodeResponseDTO> saveValidateCode(@RequestBody ValidateCodeRequest req) {
        return new ResponseEntity<>(authService.validateCode(req), HttpStatus.OK);
    }

    @PostMapping("/add-authority")
    public ResponseEntity<Access> addAuthority(@RequestParam Long accessId, @RequestParam String authorityName) {
        return new ResponseEntity<>(accessService.addAuthority(accessId, authorityName), HttpStatus.CREATED);
    }

    @DeleteMapping("/add-authority/{id}")
    public ResponseEntity<MessageDTO> removeAuthority(@PathVariable Long id) {
        return new ResponseEntity<>(accessService.removeAuthority(id), HttpStatus.OK);
    }
}
