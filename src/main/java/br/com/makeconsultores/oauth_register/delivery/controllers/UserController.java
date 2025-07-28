package br.com.makeconsultores.oauth_register.delivery.controllers;

import br.com.makeconsultores.oauth_register.infra.persistences.dtos.UpdateUserDTO;
import br.com.makeconsultores.oauth_register.infra.persistences.dtos.UserDTO;
import br.com.makeconsultores.oauth_register.infra.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static br.com.makeconsultores.oauth_register.infra.config.SecurityConfig.USER_PATH;

@RestController
@RequestMapping(USER_PATH)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDTO req) {
        return new ResponseEntity<>(userService.create(req), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> findAllUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/add-system")
    public ResponseEntity<?> addSystem(
            @RequestParam Long user,
            @RequestParam Long system
    )  {
        userService.addSystem(system, user);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findOneUser(@PathVariable Long id) {
        return new ResponseEntity<>(userService.findOne(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOneUser(@PathVariable Long id, @RequestBody UpdateUserDTO req) {
        return new ResponseEntity<>(userService.updateUser(id, req), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOneUser(@PathVariable Long id) {
        return new ResponseEntity<>(userService.delete(id), HttpStatus.OK);
    }
}
