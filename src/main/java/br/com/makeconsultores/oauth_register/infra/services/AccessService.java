package br.com.makeconsultores.oauth_register.infra.services;

import br.com.makeconsultores.oauth_register.delivery.exceptions.FieldCannotEmptyException;
import br.com.makeconsultores.oauth_register.delivery.exceptions.NotFoundException;
import br.com.makeconsultores.oauth_register.infra.persistences.Access;
import br.com.makeconsultores.oauth_register.infra.persistences.AccessRepository;
import br.com.makeconsultores.oauth_register.infra.persistences.AuthorityRepository;
import br.com.makeconsultores.oauth_register.infra.services.dtos.MessageDTO;
import br.com.makeconsultores.oauth_register.infra.services.dtos.UpdateAccessDTO;
import br.com.makeconsultores.oauth_register.infra.services.dtos.UpdateAccessWhenForgetPassword;
import br.com.makeconsultores.oauth_register.infra.services.dtos.UpdatePasswordDTO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccessService {
    private final AccessRepository accessRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;

    public AccessService(AccessRepository accessRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
        this.accessRepository = accessRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }

    public Access findOneAccess(Long id) {
        return accessRepository.findById(id).orElseThrow();
    }

    public MessageDTO updateOneAccess(Long id, UpdateAccessDTO req) {
        Access access = accessRepository.findById(id).orElseThrow(() -> new NotFoundException("Acesso não encontrado."));

        if (!req.username().isEmpty()) {
            access.setUsername(req.username());
        }

        if (req.role() != null) {
            access.setRole(req.role());
        }

        if (req.active() != null) {
            access.setActive(req.active());
        }

        accessRepository.save(access);
        return new MessageDTO("Acesso atualizado com sucesso.", 200);
    }

    public MessageDTO updatePassword(Long id, UpdatePasswordDTO req) {
        if (req.oldPassword().isEmpty()) {
            throw new FieldCannotEmptyException("Por favor, insira a sua senha atual.");
        }

        if (req.newPassword().isEmpty()) {
            throw new FieldCannotEmptyException("Por favor, insira a sua nova senha.");
        }
        //passwordValidatorService.isPasswordStrong(access.newPassword());

        if (req.confirmPassword().isEmpty()) {
            throw new FieldCannotEmptyException("Por favor, confirme a sua nova senha.");
        }

        if (!req.newPassword().equals(req.confirmPassword())) {
            throw new FieldCannotEmptyException("Nova senha é diferente da senha de confirmação.");
        }

        Access access = accessRepository.findById(id).orElseThrow(() -> new NotFoundException("Acesso não encontrado com o id: " + id));

        String encryptedPassword = access.getPassword();

        boolean isValidated = passwordEncoder.matches(req.oldPassword(), encryptedPassword);
        System.out.println(isValidated);
        if (!isValidated) {
            throw new NotFoundException("Senha atual é inválida.");
        }

        String encryptedPass = new BCryptPasswordEncoder().encode(req.newPassword());
        access.setPassword(encryptedPass);
        accessRepository.save(access);

        return new MessageDTO("Senha Atualizada com sucesso.", 200);
    }

    public MessageDTO updatePasswordWhenForgetPass(Long id, UpdateAccessWhenForgetPassword req) {
        Access entity = accessRepository.findById(id).orElseThrow(() -> new NotFoundException("Acesso não encontrado para a tualização - id: " + id));

        if (req.newPassword().isEmpty()) {
            throw new FieldCannotEmptyException("Por favor, insira a sua nova senha.");
        }
        if (req.confirmPassword().isEmpty()) {
            throw new FieldCannotEmptyException("Por favor, confirme a sua nova senha.");
        }

        if (!req.newPassword().equals(req.confirmPassword())) {
            throw new FieldCannotEmptyException("Nova senha é diferente da senha de confirmação.");
        }

        String encryptedPass = new BCryptPasswordEncoder().encode(req.newPassword());
        entity.setPassword(encryptedPass);
        accessRepository.save(entity);

        return new MessageDTO("Senha Atualizada com sucesso.", 200);
    }

        public Access addAuthority(Long accessId, String authorityName) {
        Access access = accessRepository.findById(accessId)
                .orElseThrow(() -> new RuntimeException("Access not found"));

        access.addAuthority(authorityName);
        return accessRepository.save(access);
    }

        public MessageDTO removeAuthority(Long authId) {
        if(!authorityRepository.existsById(authId)) {
            throw new NotFoundException("Authority not found.");
        }
        authorityRepository.deleteById(authId);
        return new MessageDTO("Autorização removida com sucesso.", 200);
    }
}
