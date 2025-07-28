package br.com.makeconsultores.oauth_register.infra.utils;

import br.com.makeconsultores.oauth_register.infra.persistences.Access;
import br.com.makeconsultores.oauth_register.infra.persistences.Authority;
import br.com.makeconsultores.oauth_register.infra.persistences.enums.Role;
import br.com.makeconsultores.oauth_register.infra.resources.api.NotificationApi;
import br.com.makeconsultores.oauth_register.infra.services.dtos.NotificationEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Component
public class UtilsMethods {

    private static final Logger log = LoggerFactory.getLogger(UtilsMethods.class);
    private final PasswordEncoder passwordEncoder;
    private final NotificationApi notificationApi;

    public UtilsMethods(PasswordEncoder passwordEncoder, NotificationApi notificationApi) {
        this.passwordEncoder = passwordEncoder;
        this.notificationApi = notificationApi;
    }

    private String randomPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int indice = random.nextInt(characters.length());
            password.append(characters.charAt(indice));
        }

        return password.toString();
    }

    public Access createAccess(String email, String name, String username, Role role, List<Authority> authorities) {

        String beforeEncoder = randomPassword();
        String afterEncoder = passwordEncoder.encode(beforeEncoder);

        Access access = new Access();
        access.setActive(true);
        access.setRole(role);
        access.setUsername(username);
        access.setPassword(afterEncoder);

        System.out.println("LOGIN: " + username);
        System.out.println("SENHA: " + beforeEncoder);

        // Associa cada Authority ao Access
        List<Authority> authorities1 = new ArrayList<>();

        for (Authority authority : authorities) {
            authorities1.add(authority);
            authority.setAccess(access); // define o "dono" de cada Authority
        }

        access.setAuthorities(authorities1);

        String html = generateHtml(name, username, beforeEncoder);
        NotificationEmail notify = new NotificationEmail(
                email,
                "noreply-oauth@gmail.com",
                html,
                "Cadastro de usuário",
                "",
                "",
                ""
        );
      //  notificationApi.notificationEmail(notify);

        return access;
    }

    public String generateHtml(String name, String login, String pass) {
        String html = "<!DOCTYPE html><html lang='en'>   <head>      <style>         * {            margin: 0;            padding: 0;            box-sizing: border-box;            font-family: system-ui, -apple-system, BlinkMacSystemFont,               'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans',               'Helvetica Neue', sans-serif;         }         body {            display: flex;            flex-direction: column;         }         .header {            background-color: #000000;            height: 70px;         }         .title {            font-weight: 400;            font-size: 3em;            color: #ddd;            text-align: center;         }         .middle {            padding: 2rem;            height: 500px;         }         .footer {            height: 150px;            background-color: #053;            padding: 2rem;         }         h3 {            font-weight: 400;         }         .email {            color: #333;         }         table {            background-color: #eee;         }         th {            background-color: #053;            color: #ddd;            padding: 5px;         }         td {            text-align: center;            color: #333;         }         h1,         h4 {            font-weight: 400;         }         .initial {            height: 80px;         }         .space {            height: 200px;            line-height: 200px;         }         .a {            color: #333;            font-weight: bold;            margin-top: 100px;         }         .message {            margin-bottom: 30px;         }         .content-message {            margin-top: 20px;         }         .text-message {            color: #333;         }         a,         p {            color: #fff;         }         .pass {            color: #333;         }      </style>      <meta charset='UTF-8' />      <meta name='viewport' content='width=device-width, initial-scale=1.0' />      <title>Document</title>   </head>   <body>      <div class='header'><h1 class='title'>OAUTH</h1></div>      <div class='middle'>         <div class='initial'>            <h1>Ol&aacute;, "+name+".</h1>         </div>         <div class='message'></div>         <div class='content-message'>            <h4>LOGIN: <strong>"+login+"</strong> </h4>        </div>         <p class='pass'>            PASSWORD: <strong>"+pass+"</strong>         </p>      </div>   </body></html>";
        return html;
    }
}
