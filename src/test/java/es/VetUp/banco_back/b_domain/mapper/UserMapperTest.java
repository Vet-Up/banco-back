package es.VetUp.banco_back.b_domain.mapper;

import es.VetUp.banco_back.b_domain.model.User;
import es.VetUp.banco_back.b_domain.repository.entity.UserEntity;
import es.VetUp.banco_back.b_domain.service.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserMapperTest {

    private final UserMapper userMapper = UserMapper.getInstance();

    @Test
    @DisplayName("Test fromUserToUserEntity mapping")
    void testFromUserToUserEntity() {
        User user = new User(
                1L,
                "jdoe",
                "password123",
                "John",
                "Doe",
                "Smith",
                "12345678A",
                "api-key-123");

        UserEntity userEntity = userMapper.fromUserToUserEntity(user);

        assertNotNull(userEntity);
        assertEquals(user.getUserId(), userEntity.userId());
        assertEquals(user.getUsername(), userEntity.username());
        assertEquals(user.getPassword(), userEntity.password());
        assertEquals(user.getName(), userEntity.name());
        assertEquals(user.getFirstSurname(), userEntity.firstSurname());
        assertEquals(user.getSecondSurname(), userEntity.secondSurname());
        assertEquals(user.getDni(), userEntity.dni());
        assertEquals(user.getApiKey(), userEntity.apiKey());
    }

    @Test
    @DisplayName("Test fromUserDtoToUser mapping")
    void testFromUserDtoToUser() {
        UserDto userDto = new UserDto(
                1L,
                "jdoe",
                "password123",
                "John",
                "Doe",
                "Smith",
                "12345678A",
                "api-key-123");

        User user = userMapper.fromUserDtoToUser(userDto);

        assertNotNull(user);
        assertEquals(userDto.userId(), user.getUserId());
        assertEquals(userDto.username(), user.getUsername());
        assertEquals(userDto.password(), user.getPassword());
        assertEquals(userDto.name(), user.getName());
        assertEquals(userDto.firstSurname(), user.getFirstSurname());
        assertEquals(userDto.secondSurname(), user.getSecondSurname());
        assertEquals(userDto.dni(), user.getDni());
        assertEquals(userDto.apiKey(), user.getApiKey());
    }
}
