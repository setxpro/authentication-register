package br.com.makeconsultores.oauth_register.delivery.controllers;

import br.com.makeconsultores.oauth_register.configs.NoSecurityTestConfig;
import br.com.makeconsultores.oauth_register.infra.persistences.*;
import br.com.makeconsultores.oauth_register.infra.persistences.dtos.UserDTO;
import br.com.makeconsultores.oauth_register.infra.persistences.enums.Role;
import br.com.makeconsultores.oauth_register.infra.persistences.system.SystemAccess;
import br.com.makeconsultores.oauth_register.infra.utils.UtilsMethods;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração para o {@link UserController}.
 * Verifica o comportamento da API REST para operações CRUD de usuarios.
 * Utiliza um banco de dados H2 em memória configurado no perfil "test".
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(NoSecurityTestConfig.class)
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UtilsMethods utilsMethods;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccessRepository accessRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Configuração inicial antes de cada teste.
     * Limpa o banco de dados para garantir isolamento entre os testes.
     */
    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void criar_DeveSalvarUsuarioQuandoDadosForemValidos() throws Exception {
        List<Authority> authorities = new ArrayList<>();
        UserDTO req = new UserDTO(
                "Teste Testando",
                "test.testando@teste.com.br",
                "5521955554444",
                Role.ADMIN,
                "teste.testando",
                authorities
        );

        Access access = utilsMethods.createAccess(
                req.email(),
                req.name(),
                req.username(),
                req.role(),
                req.authorities()
        );

        // Salva o access antes
        access = accessRepository.save(access);

        List<SystemAccess> systems = new ArrayList<>();
        User user = new User(
                null,
                req.name(),
                req.email(),
                req.phone(),
                access,
                systems
        );

        mockMvc.perform(
                post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.status", is(201)));
    }

    @Test
    void listarTodos_DeveRetornarUsuariosQuandoExitirem() throws Exception {

        mockMvc.perform(
                    get("/user")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }


    @Test
    void findOneUser() throws Exception {
        List<Authority> authorities = new ArrayList<>();
        UserDTO req = new UserDTO(
                "Teste Testando",
                "test.testando@teste.com.br",
                "5521955554444",
                Role.ADMIN,
                "teste.testando",
                authorities
        );

        Access access = utilsMethods.createAccess(
                req.email(),
                req.name(),
                req.username(),
                req.role(),
                req.authorities()
        );

        // Salva o access antes
        access = accessRepository.save(access);

        List<SystemAccess> systems = new ArrayList<>();
        User user = userRepository.save(
                new User(
                        null,
                        req.name(),
                        req.email(),
                        req.phone(),
                        access,
                        systems
                )
        );

        mockMvc.perform(get("/user/{id}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Teste Testando")))
                .andExpect(jsonPath("$.phone", is("5521955554444")));
    }

    @Test
    void buscarPorId_DeveRetornarNotFoundQuandoNaoExistir() throws Exception {
        mockMvc.perform(get("/user/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("Usuário não encontrado.")))
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    void updateOneUser() {
    }

    @Test
    void deleteOneUser() {
    }
}