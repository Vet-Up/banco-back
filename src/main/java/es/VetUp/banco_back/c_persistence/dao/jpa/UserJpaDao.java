package es.VetUp.banco_back.c_persistence.dao.jpa;

import es.VetUp.banco_back.b_domain.model.User;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.UserJpaEntity;

import java.util.Optional;

public interface UserJpaDao {
    Optional<UserJpaEntity> getByUsername(String username);
    Optional<UserJpaEntity> getByDni(String dni);
    Optional<Boolean> existsByApiKey(String username, String apiKey);
    Long count();
}
