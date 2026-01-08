package es.VetUp.banco_back.b_domain.repository.entity;

public record UserEntity (
        Long userId,
        String username,
        String password,
        String name,
        String firstSurname,
        String secondSurname,
        String dni,
        String apiKey
){
}
