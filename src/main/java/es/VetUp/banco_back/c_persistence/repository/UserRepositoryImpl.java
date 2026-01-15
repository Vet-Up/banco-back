package es.VetUp.banco_back.c_persistence.repository;

import es.VetUp.banco_back.b_domain.repository.UserRepository;
import es.VetUp.banco_back.b_domain.repository.entity.UserEntity;
import es.VetUp.banco_back.c_persistence.dao.jpa.UserJpaDao;
import es.VetUp.banco_back.c_persistence.repository.mapper.UserPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaDao userJpaDao;

    public UserRepositoryImpl(UserJpaDao userJpaDao) {
        this.userJpaDao = userJpaDao;
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userJpaDao.getByUsername(username)
            .map(UserPersistenceMapper.getInstance()::fromUserJpaEntityToUserEntity);
    }

    @Override
    public Optional<UserEntity> findByDni(String dni) {
        return userJpaDao.getByDni(dni)
            .map(UserPersistenceMapper.getInstance()::fromUserJpaEntityToUserEntity);
    }

    @Override
    public Optional<Boolean> existsByApiKey(String username, String apiKey) {
        return userJpaDao.existsByApiKey(username, apiKey);
    }

    @Override
    public Optional<UserEntity> findById(Long userId) {
        return userJpaDao.getById(userId)
            .map(UserPersistenceMapper.getInstance()::fromUserJpaEntityToUserEntity);
    }
}
