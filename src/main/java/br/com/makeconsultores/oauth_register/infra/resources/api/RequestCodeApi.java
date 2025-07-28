package br.com.makeconsultores.oauth_register.infra.resources.api;

import br.com.makeconsultores.oauth_register.infra.services.dtos.RequestCode;
import br.com.makeconsultores.oauth_register.infra.services.dtos.ResponseCode;
import br.com.makeconsultores.oauth_register.infra.services.dtos.ValidateCodeRequest;
import br.com.makeconsultores.oauth_register.infra.services.dtos.ValidateCodeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "validateHash", url = "${integration.api.validate}")
public interface RequestCodeApi {
    @PostMapping("/generate")
    ResponseEntity<ResponseCode> generateCode(@RequestBody RequestCode req);

    @PostMapping("/validate")
    ResponseEntity<ValidateCodeResponse> validateCode(@RequestBody ValidateCodeRequest req);
}

