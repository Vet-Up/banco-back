package es.VetUp.banco_back.c_persistence.repository.mapper;

import es.VetUp.banco_back.b_domain.repository.entity.UserEntity;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.UserJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserPersistenceMapperTest {

    private final UserPersistenceMapper mapper = UserPersistenceMapper.getInstance();

    @Nested
    class FromUserJpaEntityToUserEntityTests {

        @Test
        @DisplayName("should map JpaEntity to UserEntity correctly")
        void testMapping() {
            UserJpaEntity jpaEntity = new UserJpaEntity();
            jpaEntity.setUserId(1L);
            jpaEntity.setUsername("johndoe");
            jpaEntity.setPassword("password123");
            jpaEntity.setName("John");
            jpaEntity.setFirstSurname("Doe");
            jpaEntity.setSecondSurname("Smith");
            jpaEntity.setDni("12345678A");
            jpaEntity.setApiKey("api_key_123");

            UserEntity result = mapper.fromUserJpaEntityToUserEntity(jpaEntity);

            assertAll("result",
                    () -> assertEquals(1L, result.userId()),
                    () -> assertEquals("johndoe", result.username()),
                    () -> assertEquals("password123", result.password()),
                    () -> assertEquals("John", result.name()),
                    () -> assertEquals("Doe", result.firstSurname()),
                    () -> assertEquals("Smith", result.secondSurname()),
                    () -> assertEquals("12345678A", result.dni()),
                    () -> assertEquals("api_key_123", result.apiKey())
            );
        }

        @Test
        @DisplayName("should return null when input is null")
        void testNullInput() {
            UserEntity result = mapper.fromUserJpaEntityToUserEntity(null);

            assertNull(result);
        }
    }

    @Nested
    class FromUserEntityToUserJpaEntityTests {

        @Test
        @DisplayName("should map UserEntity to JpaEntity correctly")
        void testMapping() {
            UserEntity entity = new UserEntity(
                    1L,
                    "janedoe",
                    "secret456",
                    "Jane",
                    "Doe",
                    "Johnson",
                    "87654321B",
                    "api_key_456"
            );

            UserJpaEntity result = mapper.fromUserEntityToUserJpaEntity(entity);

            assertAll("result",
                    () -> assertEquals(1L, result.getUserId()),
                    () -> assertEquals("janedoe", result.getUsername()),
                    () -> assertEquals("secret456", result.getPassword()),
                    () -> assertEquals("Jane", result.getName()),
                    () -> assertEquals("Doe", result.getFirstSurname()),
                    () -> assertEquals("Johnson", result.getSecondSurname()),
                    () -> assertEquals("87654321B", result.getDni()),
                    () -> assertEquals("api_key_456", result.getApiKey())
            );
        }

        @Test
        @DisplayName("should return null when input is null")
        void testNullInput() {
            UserJpaEntity result = mapper.fromUserEntityToUserJpaEntity(null);

            assertNull(result);
        }
    }

    @Test
    @DisplayName("getInstance should return singleton instance")
    void testSingleton() {
        UserPersistenceMapper instance1 = UserPersistenceMapper.getInstance();
        UserPersistenceMapper instance2 = UserPersistenceMapper.getInstance();

        assertSame(instance1, instance2);
    }
}
