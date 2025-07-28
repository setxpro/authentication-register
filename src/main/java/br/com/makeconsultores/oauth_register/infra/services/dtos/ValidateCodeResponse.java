package br.com.makeconsultores.oauth_register.infra.services.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ValidateCodeResponse(
        @JsonProperty("_id")
        String id,
        String hash,
        Long userId,
        String endDate,
        String createdAt,
        String updatedAt,

        @JsonProperty("__v")
        int version
) {
}
