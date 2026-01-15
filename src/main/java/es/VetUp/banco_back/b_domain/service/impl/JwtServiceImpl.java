package es.VetUp.banco_back.b_domain.service.impl;

import es.VetUp.banco_back.b_domain.service.JwtService;
import es.VetUp.banco_back.b_domain.service.UserService;
import es.VetUp.banco_back.b_domain.service.dto.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtServiceImpl implements JwtService {

    private final UserService userService;

    public JwtServiceImpl(UserService userService) {
        this.userService = userService;
    }

    private static final long EXPIRATION_TIME = 8640000000L;
    private static final String SECRET = "mi_clave_super_secreta_muy_larga_minimo_32_bytes!!";

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());


    @Override
    public String generateToken(UserDto user) {
        return Jwts.builder()
                .claim("id",user.userId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    @Override
    public Claims validateToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Override
    public UserDto getUserFromToken(String token) {
        Claims claims = validateToken(token);
        Long userId = claims.get("id", Long.class);
        return userService.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
