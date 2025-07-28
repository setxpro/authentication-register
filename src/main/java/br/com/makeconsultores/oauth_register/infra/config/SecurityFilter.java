package br.com.makeconsultores.oauth_register.infra.config;

import br.com.makeconsultores.oauth_register.delivery.dtos.MessageDTO;
import br.com.makeconsultores.oauth_register.infra.persistences.AccessRepository;
import br.com.makeconsultores.oauth_register.infra.services.TokenService;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class SecurityFilter extends OncePerRequestFilter {

    private final AccessRepository accessRepository;
    private final TokenService tokenService;

    public SecurityFilter(AccessRepository accessRepository, TokenService tokenService) {
        this.accessRepository = accessRepository;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {

            String uri = request.getRequestURI();
            System.out.println("CALLED URI" + uri);

            String method = request.getMethod();
            System.out.println("CALLED METHOD" + method);

            // Lista de endpoints públicos
            boolean isPublic =
                    (method.equals("POST") && (uri.equals("/api/sdu/auth/sign-in") ||
                            uri.equals("/api/sdu/auth/logout") ||
                            uri.equals("/api/sdu/auth/validate-code") ||
                            uri.equals("/api/sdu/auth/forget-password") ||
                            uri.startsWith("/api/sdu/auth/update-password-forget"))) ||
                            uri.equals("/api/sdu/auth/add-authority") ||

                            (method.equals("POST") && uri.equals("/api/sdu/user")) ||
                            (method.equals("GET") && (uri.startsWith("/api/sdu/user"))) ||
                            (method.equals("PUT") && uri.startsWith("/api/sdu/user")) ||
                            (method.equals("DELETE") && uri.startsWith("/api/sdu/user")) ||

                            (method.equals("GET") && uri.startsWith("/access/")) ||

                            (method.equals("PUT") && (
                                    uri.startsWith("/access/update-access/") ||
                                            uri.startsWith("/access/update-password/") ||
                                            uri.startsWith("/access/password-forgot/"))) ||

                            (method.equals("POST") && uri.startsWith("/system")) ||
                            (method.equals("GET") && uri.startsWith("/system")) ||
                            (method.equals("PUT") && uri.startsWith("/system")) ||
                            (method.equals("DELETE") && uri.startsWith("/system"));

            if (isPublic) {
                // Pula autenticação e segue
                filterChain.doFilter(request, response);
                return;
            }

            var token = recoverToken(request);

            if (token == null) {
                token = recoverTokenCookie(request);
            }

            if (token != null) {
                DecodedJWT decodedJWT = tokenService.validateToken(token);

                String username = decodedJWT.getSubject();
                String[] roles = decodedJWT.getClaim("roles").asArray(String.class);

                List<GrantedAuthority> authorities = Arrays.stream(roles)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                var authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);
        }
        catch (Exception ex) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            String json = new ObjectMapper().writeValueAsString(new MessageDTO(ex.getMessage(), HttpStatus.UNAUTHORIZED.value()));
            response.getWriter().write(json);
            return;
        }
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        logger.info("AUTH HEADER: " + authHeader);
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }

    private String recoverTokenCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }



}
