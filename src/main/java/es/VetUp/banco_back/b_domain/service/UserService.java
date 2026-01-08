package es.VetUp.banco_back.b_domain.service;

import es.VetUp.banco_back.b_domain.model.User;
import es.VetUp.banco_back.b_domain.service.dto.UserDto;

import java.util.Optional;

public interface UserService {
    UserDto authenticate(String username, String apiKey);
    Optional<UserDto> findByUsername(String username);
    Optional<UserDto> findByDni(String dni);




}
