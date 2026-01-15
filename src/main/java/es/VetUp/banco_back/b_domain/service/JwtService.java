package es.VetUp.banco_back.b_domain.service;

import es.VetUp.banco_back.b_domain.service.dto.UserDto;
import io.jsonwebtoken.Claims;

public interface JwtService {

    String generateToken(UserDto user);
    Claims validateToken(String token);
    UserDto getUserFromToken(String token);

}
