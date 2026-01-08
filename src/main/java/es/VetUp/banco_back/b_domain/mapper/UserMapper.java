package es.VetUp.banco_back.b_domain.mapper;

import es.VetUp.banco_back.b_domain.model.User;
import es.VetUp.banco_back.b_domain.repository.entity.UserEntity;
import es.VetUp.banco_back.b_domain.service.dto.UserDto;

public class UserMapper {
    private static UserMapper INSTANCE;

    private UserMapper() {
    }

    public static UserMapper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserMapper();
        }
        return INSTANCE;
    }

    public User fromUserEntityToUser(UserEntity userEntity) {
        return new User(
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

    public UserEntity fromUserToUserEntity(User user) {
        return new UserEntity(
                user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getFirstSurname(),
                user.getSecondSurname(),
                user.getDni(),
                user.getApiKey()
        );
    }

    public User fromUserDtoToUser(UserDto userDto) {
        return new User(
                userDto.userId(),
                userDto.username(),
                userDto.password(),
                userDto.name(),
                userDto.firstSurname(),
                userDto.secondSurname(),
                userDto.dni(),
                userDto.apiKey()
        );
    }

    public UserDto fromUserToUserDto(User user) {
        return new UserDto(
                user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getFirstSurname(),
                user.getSecondSurname(),
                user.getDni(),
                user.getApiKey()
        );
    }
}
