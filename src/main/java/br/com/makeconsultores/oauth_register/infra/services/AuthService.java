package br.com.makeconsultores.oauth_register.infra.services;

import br.com.makeconsultores.oauth_register.delivery.exceptions.*;
import br.com.makeconsultores.oauth_register.infra.persistences.Access;
import br.com.makeconsultores.oauth_register.infra.persistences.AccessRepository;
import br.com.makeconsultores.oauth_register.infra.persistences.User;
import br.com.makeconsultores.oauth_register.infra.persistences.UserRepository;
import br.com.makeconsultores.oauth_register.infra.persistences.dtos.UserSignInDTO;
import br.com.makeconsultores.oauth_register.infra.resources.api.RequestCodeApi;
import br.com.makeconsultores.oauth_register.infra.services.dtos.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AccessRepository accessRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RequestCodeApi requestCodeApi;

    public UserSignInDTO signin(SignInRequestDTO req, HttpServletResponse response) {
        log.info("REQUEST SIGN IN");
        try {

            var auth = accessRepository.findByUsername(req.username());

            if (auth == null) {
                log.warn("(SIGN IN): USER NOT FOUND.");
                throw new NotFoundException("Usuário não encontrado.");
            }

            if (!auth.isAccountNonLocked()) {
                log.warn("(SIGN IN): ACCESS BLOCKED");
                throw new UnauthorizedAccessException("Acesso bloqueado.");
            }

            // collaboratorRepository.findAccessByUsername(auth.getUsername()).orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

            var usernamePassword = new UsernamePasswordAuthenticationToken(req.username(), req.password());
            var authenticate = this.authenticationManager.authenticate(usernamePassword);
            var token = tokenService.generateToken((Access) authenticate.getPrincipal());

            UserDetails user = accessRepository.findByUsername(req.username());
            Access access = accessRepository.findAccQuery(req.username()).orElseThrow(() -> new NotFoundException("Acesso não encontrado."));
            User userInfo = userRepository.findByAccess_Id(access.getId()).orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

            // ADD TOKEN
            setTokenCookie(response, token);

            return UserSignInDTO.toDto(userInfo, access, user, token);

        } catch (BadCredentialsException ex) {
            log.error("(SIGN IN): ERROR WHEN REQUEST SIGN IN: {}", ex.getMessage());
            throw new InvalidPasswordException("Senha inválida para o usuário " + req.username());
        }
    }

    private void setTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(259200); // tempo de vida para 72h
        response.addCookie(cookie);
    }

    public MessageForgetDTO forgetPassword(ForgetPasswordRequest req) {
        if (req.email() == null || req.email().isEmpty()) {
            throw new FieldCannotEmptyException("Por favor, insira ao menos um e-mail.");
        }

        User user = userRepository.findByEmail(req.email()).orElseThrow();
        RequestCode getCode = new RequestCode(user.getId().toString(), user.getEmail(), user.getName());
        requestCodeApi.generateCode(getCode);

        return new MessageForgetDTO("Código de verificação enviado para o E-mail: " + user.getEmail(), 200, user.getId().toString());
    }

    public ValidateCodeResponseDTO validateCode(ValidateCodeRequest req) {
        if (
                req.hash() != null && !req.hash().isEmpty() &&
                        req.userId() != null && !req.userId().isEmpty()) {
            try {
                ValidateCodeResponse validateCodeResponse = requestCodeApi.validateCode(req).getBody();
                System.out.println(validateCodeResponse);
                return new ValidateCodeResponseDTO("Autenticação realizada com sucesso.", true, validateCodeResponse.userId());
            } catch (Exception e) {
                throw new BadValidationException("O Códigode verificação é inválido.");
            }
        }

        throw new BadValidationException("Erro na tentativa de validar o código. Fale com o TI.");
    }
}
