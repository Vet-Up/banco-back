package es.VetUp.banco_back.c_persistence.repository.mapper;

import es.VetUp.banco_back.b_domain.repository.entity.UserEntity;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.UserJpaEntity;

public class UserPersistenceMapper {

    private static UserPersistenceMapper INSTANCE;
    private UserPersistenceMapper() {
    }
    public static UserPersistenceMapper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserPersistenceMapper();
        }
        return INSTANCE;
    }

    public UserEntity fromUserJpaEntityToUserEntity(UserJpaEntity userJpaEntity) {
        if (userJpaEntity == null) {
            return null;
        }
        return new UserEntity(
                userJpaEntity.getUserId(),
                userJpaEntity.getUsername(),
                userJpaEntity.getPassword(),
                userJpaEntity.getName(),
                userJpaEntity.getFirstSurname(),
                userJpaEntity.getSecondSurname(),
                userJpaEntity.getDni(),
                userJpaEntity.getApiKey()
        );

    }

    public UserJpaEntity fromUserEntityToUserJpaEntity(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        return new UserJpaEntity(
                userEntity.userId(),
                userEntity.username(),
                userEntity.password(),
                userEntity.name(),
                userEntity.firstSurname(),
                userEntity.secondSurname(),
                userEntity.dni(),
                userEntity.apiKey()
        );
    }


}
