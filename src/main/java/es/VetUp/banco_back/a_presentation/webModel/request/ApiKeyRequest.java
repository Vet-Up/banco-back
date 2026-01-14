package es.VetUp.banco_back.a_presentation.webModel.request;

import jakarta.validation.constraints.NotBlank;

public record ApiKeyRequest(
        @NotBlank(message = "API key must not be blank")
        String apiKey,
        @NotBlank(message = "Username must not be blank")
        String username
) {
}
