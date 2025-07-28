package br.com.makeconsultores.oauth_register.infra.config;

import br.com.makeconsultores.oauth_register.delivery.dtos.MessageDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    public static final String AUTH_PATH = "/auth";
    public static final String SYSTEM_PATH = "/system";
    public static final String USER_PATH = "/user";
    public static final String ACCESS_PATH = "/access";


    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        try {

            return httpSecurity
                    .csrf(AbstractHttpConfigurer::disable)
                    .exceptionHandling(exception -> exception
                            .authenticationEntryPoint((request, response, authException) -> {
                                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                                // Exemplo de mensagem de erro
                                String json = new ObjectMapper()
                                        .writeValueAsString(new MessageDTO("Usuário precisa estar autenticado para realizar esta ação!", HttpStatus.UNAUTHORIZED.value()));
                                response.getWriter().write(json);
                            })
                            .accessDeniedHandler((request, response, accessDeniedException) -> {
                                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                                response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                                String json = new ObjectMapper()
                                        .writeValueAsString(new MessageDTO("Usuário não tem permissão para executar essa tarefa!", HttpStatus.FORBIDDEN.value()));
                                response.getWriter().write(json);
                            })
                    )
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(authorize -> authorize
                            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                            .requestMatchers(HttpMethod.POST,AUTH_PATH + "/sign-in").permitAll()
                            .requestMatchers(HttpMethod.POST, AUTH_PATH + "/add-authority").permitAll()
                            .requestMatchers(HttpMethod.DELETE, AUTH_PATH + "/add-authority/**").permitAll()
                            .requestMatchers(HttpMethod.POST,AUTH_PATH + "/logout").permitAll()
                            .requestMatchers(HttpMethod.POST,AUTH_PATH + "/validate-code").permitAll()
                            .requestMatchers(HttpMethod.POST,AUTH_PATH + "/forget-password").permitAll()
                            .requestMatchers(HttpMethod.PUT,AUTH_PATH + "/update-password-forget/**").permitAll()

                            .requestMatchers(HttpMethod.POST,USER_PATH).permitAll()
                            .requestMatchers(HttpMethod.GET,USER_PATH).permitAll()
                            .requestMatchers(HttpMethod.GET,USER_PATH + "/**").permitAll()
                            .requestMatchers(HttpMethod.PUT,USER_PATH + "/**").permitAll()
                            .requestMatchers(HttpMethod.DELETE,USER_PATH + "/**").permitAll()

                            .requestMatchers(HttpMethod.GET,ACCESS_PATH + "/**").permitAll()
                            .requestMatchers(HttpMethod.PUT,ACCESS_PATH + "/update-access/**").permitAll()
                            .requestMatchers(HttpMethod.PUT,ACCESS_PATH + "/update-password/**").permitAll()
                            .requestMatchers(HttpMethod.PUT,ACCESS_PATH + "/password-forgot/**").permitAll()

                            .requestMatchers(HttpMethod.POST,SYSTEM_PATH).permitAll()
                            .requestMatchers(HttpMethod.GET,SYSTEM_PATH).permitAll()
                            .requestMatchers(HttpMethod.GET,SYSTEM_PATH + "/**").permitAll()
                            .requestMatchers(HttpMethod.PUT,SYSTEM_PATH + "/**").permitAll()
                            .requestMatchers(HttpMethod.DELETE,SYSTEM_PATH + "/**").permitAll()

                    ).addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();
        }
        catch (Exception e) {
            throw new InternalException("Erro Interno: " + e.getMessage());
        }
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Crypt password
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
