package br.com.makeconsultores.oauth_register.infra.resources.api;

import br.com.makeconsultores.oauth_register.infra.services.dtos.MessageDTO;
import br.com.makeconsultores.oauth_register.infra.services.dtos.NotificationEmail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "notificationApi", url = "http://172.16.10.211:7000/api/notification")
public interface NotificationApi {

    @PostMapping("/gmail/email")
    ResponseEntity<MessageDTO> notificationEmail(NotificationEmail req);
}
