package es.VetUp.banco_back.c_persistence.dao.jpa.impl;

import es.VetUp.banco_back.b_domain.repository.entity.UserEntity;
import es.VetUp.banco_back.c_persistence.dao.jpa.UserJpaDao;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.UserJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Optional;

public class UserJpaDaoImpl implements UserJpaDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<UserJpaEntity> getByUsername(String username) {
        String sql = "SELECT u FROM UserJpaEntity u WHERE u.username = :username";
        try {
            return Optional.of(entityManager.createQuery(sql, UserJpaEntity.class)
                    .setParameter("username", username)
                    .getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserJpaEntity> getByDni(String dni) {
        String sql = "SELECT u FROM UserJpaEntity u WHERE u.dni = :dni";
        try {
            return Optional.of(entityManager.createQuery(sql, UserJpaEntity.class)
                    .setParameter("dni", dni)
                    .getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Boolean> existsByApiKey(String username, String apiKey) {
       String query = "SELECT COUNT(u) FROM UserJpaEntity u WHERE u.username = :username AND u.apiKey = :apiKey";
       Long count = entityManager.createQuery(query, Long.class)
               .setParameter("username", username)
               .setParameter("apiKey", apiKey)
               .getSingleResult();
       return Optional.of(count > 0);
    }

    @Override
    public Long count() {
        return entityManager.createQuery("SELECT COUNT(u) FROM UserJpaEntity u", Long.class)
                .getSingleResult();
    }

    @Override
    public Optional<UserJpaEntity> getById(Long userId) {
        String sql = "SELECT u FROM UserJpaEntity u WHERE u.userId = :userId";
        try {
            return Optional.of(entityManager.createQuery(sql, UserJpaEntity.class)
                    .setParameter("userId", userId)
                    .getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }


}
