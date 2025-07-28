package br.com.makeconsultores.oauth_register.infra.services;

import br.com.makeconsultores.oauth_register.delivery.dtos.MessageDTO;
import br.com.makeconsultores.oauth_register.delivery.exceptions.NotFoundException;
import br.com.makeconsultores.oauth_register.infra.persistences.Access;
import br.com.makeconsultores.oauth_register.infra.persistences.AccessRepository;
import br.com.makeconsultores.oauth_register.infra.persistences.User;
import br.com.makeconsultores.oauth_register.infra.persistences.UserRepository;
import br.com.makeconsultores.oauth_register.infra.persistences.dtos.UpdateUserDTO;
import br.com.makeconsultores.oauth_register.infra.persistences.dtos.UserDTO;
import br.com.makeconsultores.oauth_register.infra.persistences.system.SystemAccess;
import br.com.makeconsultores.oauth_register.infra.utils.UtilsMethods;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UtilsMethods utilsMethods;
    private final UserRepository userRepository;
    private final AccessRepository accessRepository;
    private final SystemAccessService systemAccessService;

    public UserService(UtilsMethods utilsMethods, UserRepository userRepository, AccessRepository accessRepository, SystemAccessService systemAccessService) {
        this.utilsMethods = utilsMethods;
        this.userRepository = userRepository;
        this.accessRepository = accessRepository;
        this.systemAccessService = systemAccessService;
    }

    // CREATE USER
    public MessageDTO create(UserDTO req) {

        // TODO: IT WILL CREATE EXCEPTIONS
        if (req.name().isEmpty()) {}
        if (req.email().isEmpty()) {}
        if (req.role() == null) {}
        if (req.username().isEmpty()) {}

        // OBJECT: USER
        User user = new User(req);

        List<SystemAccess> systemAccess = new ArrayList<>();

        user.setSystems(systemAccess);

        Access access = utilsMethods.createAccess(req.email(), req.name(), req.username(), req.role(), req.authorities());
        Access savedAccess = accessRepository.save(access);

        user.setAccess(savedAccess);

        userRepository.save(user);

        return new MessageDTO("Usuário Cadastrado com sucesso.", 201);
    }

    // FIND ALL USERS
    public List<User> findAll() {
        return userRepository.findAll();
    }

    // FIND ONE USER
    public User findOne(Long id) {
        // TODO: IT WILL CREATE EXCEPTION
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
    }


    // UPDATE USER
    public MessageDTO updateUser(Long id, UpdateUserDTO req) {
        // TODO: IT WILL CREATE EXCEPTION
        User userExists = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        // validations
        if (!req.name().isEmpty()) {userExists.setName(req.name());}
        if (!req.email().isEmpty()) {userExists.setEmail(req.email());}
        if (!req.phone().isEmpty()) {userExists.setPhone(req.phone());}

        userRepository.save(userExists);
        return new MessageDTO("Usuário atualizado com sucesso.", 200);
    }

    // DELETE USER
    public MessageDTO delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
        userRepository.deleteById(user.getId());
        accessRepository.deleteById(user.getAccess().getId());
        return new MessageDTO("Usuário removido com sucesso.", 200);
    }

    @Transactional
    public void addSystem(Long systemId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
        SystemAccess system = systemAccessService.findOneSystem(systemId);

        if (!user.getSystems().contains(system)) {
            user.getSystems().add(system);
            userRepository.save(user);
        }
    }
}
