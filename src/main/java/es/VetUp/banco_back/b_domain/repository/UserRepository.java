package es.VetUp.banco_back.b_domain.repository;

import es.VetUp.banco_back.b_domain.model.User;
import es.VetUp.banco_back.b_domain.repository.entity.UserEntity;

import java.util.Optional;

public interface UserRepository {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByDni(String dni);
    Optional<Boolean> existsByApiKey(String username,String apiKey);
    Optional<UserEntity> findById(Long userId);

}
