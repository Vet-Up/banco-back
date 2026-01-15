package es.VetUp.banco_back.c_persistence.repository;

import es.VetUp.banco_back.b_domain.repository.entity.UserEntity;
import es.VetUp.banco_back.c_persistence.dao.jpa.UserJpaDao;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.UserJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {

    @Mock
    private UserJpaDao userJpaDao;

    @InjectMocks
    private UserRepositoryImpl userRepository;

    private UserJpaEntity testJpaEntity;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepositoryImpl(userJpaDao);
        testJpaEntity = new UserJpaEntity();
        testJpaEntity.setUserId(1L);
        testJpaEntity.setUsername("johndoe");
        testJpaEntity.setPassword("password123");
        testJpaEntity.setName("John");
        testJpaEntity.setFirstSurname("Doe");
        testJpaEntity.setSecondSurname("Smith");
        testJpaEntity.setDni("12345678A");
        testJpaEntity.setApiKey("api_key_123");
    }

    @Nested
    class FindByUsernameTests {

        @Test
        @DisplayName("findByUsername should return user when found")
        void testFindByUsername() {
            String username = "johndoe";
            when(userJpaDao.getByUsername(username)).thenReturn(Optional.of(testJpaEntity));

            Optional<UserEntity> result = userRepository.findByUsername(username);

            assertAll("result",
                    () -> assertTrue(result.isPresent()),
                    () -> assertEquals(testJpaEntity.getUserId(), result.get().userId()),
                    () -> assertEquals(testJpaEntity.getUsername(), result.get().username())
            );
        }

        @Test
        @DisplayName("findByUsername should return empty when not found")
        void testFindByUsernameNotFound() {
            String username = "nonexistent";
            when(userJpaDao.getByUsername(username)).thenReturn(Optional.empty());

            Optional<UserEntity> result = userRepository.findByUsername(username);

            assertFalse(result.isPresent());
        }
    }

    @Nested
    class FindByDniTests {

        @Test
        @DisplayName("findByDni should return user when found")
        void testFindByDni() {
            String dni = "12345678A";
            when(userJpaDao.getByDni(dni)).thenReturn(Optional.of(testJpaEntity));

            Optional<UserEntity> result = userRepository.findByDni(dni);

            assertAll("result",
                    () -> assertTrue(result.isPresent()),
                    () -> assertEquals(testJpaEntity.getUserId(), result.get().userId()),
                    () -> assertEquals(testJpaEntity.getDni(), result.get().dni())
            );
        }

        @Test
        @DisplayName("findByDni should return empty when not found")
        void testFindByDniNotFound() {
            String dni = "00000000X";
            when(userJpaDao.getByDni(dni)).thenReturn(Optional.empty());

            Optional<UserEntity> result = userRepository.findByDni(dni);

            assertFalse(result.isPresent());
        }
    }

    @Nested
    class ExistsByApiKeyTests {

        @Test
        @DisplayName("existsByApiKey should return true when valid")
        void testExistsByApiKeyValid() {
            String username = "johndoe";
            String apiKey = "api_key_123";
            when(userJpaDao.existsByApiKey(username, apiKey)).thenReturn(Optional.of(true));

            Optional<Boolean> result = userRepository.existsByApiKey(username, apiKey);

            assertAll("result",
                    () -> assertTrue(result.isPresent()),
                    () -> assertTrue(result.get())
            );
        }

        @Test
        @DisplayName("existsByApiKey should return false when invalid")
        void testExistsByApiKeyInvalid() {
            String username = "johndoe";
            String apiKey = "wrong_key";
            when(userJpaDao.existsByApiKey(username, apiKey)).thenReturn(Optional.of(false));

            Optional<Boolean> result = userRepository.existsByApiKey(username, apiKey);

            assertAll("result",
                    () -> assertTrue(result.isPresent()),
                    () -> assertFalse(result.get())
            );
        }

        @Test
        @DisplayName("existsByApiKey should return empty when user not found")
        void testExistsByApiKeyNotFound() {
            String username = "nonexistent";
            String apiKey = "api_key";
            when(userJpaDao.existsByApiKey(username, apiKey)).thenReturn(Optional.empty());

            Optional<Boolean> result = userRepository.existsByApiKey(username, apiKey);

            assertFalse(result.isPresent());
        }
    }

    @Nested
    class FindByIdTests {

        @Test
        @DisplayName("findById should return user when found")
        void testFindById() {
            Long userId = 1L;
            when(userJpaDao.getById(userId)).thenReturn(Optional.of(testJpaEntity));

            Optional<UserEntity> result = userRepository.findById(userId);

            assertAll("result",
                    () -> assertTrue(result.isPresent()),
                    () -> assertEquals(testJpaEntity.getUserId(), result.get().userId()),
                    () -> assertEquals(testJpaEntity.getUsername(), result.get().username())
            );
        }

        @Test
        @DisplayName("findById should return empty when not found")
        void testFindByIdNotFound() {
            Long userId = 999L;
            when(userJpaDao.getById(userId)).thenReturn(Optional.empty());

            Optional<UserEntity> result = userRepository.findById(userId);

            assertFalse(result.isPresent());
        }
    }
}
