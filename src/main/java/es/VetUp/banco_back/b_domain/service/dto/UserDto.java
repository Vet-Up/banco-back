package es.VetUp.banco_back.b_domain.service.dto;

import jakarta.validation.constraints.NotNull;

public record UserDto (
        Long userId,
        @NotNull
        String username,
        @NotNull
        String password,
        @NotNull
        String name,
        String firstSurname,
        @NotNull
        String secondSurname,
        @NotNull
        String dni,
        String apiKey
){
}
