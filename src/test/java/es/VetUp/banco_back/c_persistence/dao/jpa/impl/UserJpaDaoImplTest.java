package es.VetUp.banco_back.c_persistence.dao.jpa.impl;

import es.VetUp.banco_back.c_persistence.TestConfig;
import es.VetUp.banco_back.c_persistence.dao.jpa.UserJpaDao;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.UserJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestConfig.class)
class UserJpaDaoImplTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserJpaDao userJpaDao;

    private UserJpaEntity createAndPersistUser(String suffix, String username, String dni, String apiKey) {
        UserJpaEntity user = new UserJpaEntity();
        user.setUsername(username);
        user.setPassword("password123");
        user.setName("Test" + suffix);
        user.setFirstSurname("User");
        user.setSecondSurname("UD");
        user.setDni(dni);
        user.setApiKey(apiKey);
        entityManager.persist(user);
        entityManager.flush();
        return user;
    }

    @Nested
    class GetByUsernameTests {

        @Test
        @DisplayName("getByUsername should return user when found")
        void testGetByUsername() {
            createAndPersistUser("001", "userdao001", "UD00001A", "apikey_ud_001");

            Optional<UserJpaEntity> result = userJpaDao.getByUsername("userdao001");

            assertAll("result",
                    () -> assertTrue(result.isPresent()),
                    () -> assertEquals("userdao001", result.get().getUsername()),
                    () -> assertEquals("UD00001A", result.get().getDni())
            );
        }

        @Test
        @DisplayName("getByUsername should return empty when not found")
        void testGetByUsernameNotFound() {
            Optional<UserJpaEntity> result = userJpaDao.getByUsername("nonexistent_user");

            assertFalse(result.isPresent());
        }
    }

    @Nested
    class GetByDniTests {

        @Test
        @DisplayName("getByDni should return user when found")
        void testGetByDni() {
            createAndPersistUser("002", "userdao002", "UD00002B", "apikey_ud_002");

            Optional<UserJpaEntity> result = userJpaDao.getByDni("UD00002B");

            assertAll("result",
                    () -> assertTrue(result.isPresent()),
                    () -> assertEquals("userdao002", result.get().getUsername()),
                    () -> assertEquals("UD00002B", result.get().getDni())
            );
        }

        @Test
        @DisplayName("getByDni should return empty when not found")
        void testGetByDniNotFound() {
            Optional<UserJpaEntity> result = userJpaDao.getByDni("XX00000X");

            assertFalse(result.isPresent());
        }
    }

    @Nested
    class ExistsByApiKeyTests {

        @Test
        @DisplayName("existsByApiKey should return true when credentials match")
        void testExistsByApiKeyValid() {
            createAndPersistUser("003", "userdao003", "UD00003C", "apikey_ud_003");

            Optional<Boolean> result = userJpaDao.existsByApiKey("userdao003", "apikey_ud_003");

            assertAll("result",
                    () -> assertTrue(result.isPresent()),
                    () -> assertTrue(result.get())
            );
        }

        @Test
        @DisplayName("existsByApiKey should return false when apiKey doesn't match")
        void testExistsByApiKeyInvalidKey() {
            createAndPersistUser("004", "userdao004", "UD00004D", "apikey_ud_004");

            Optional<Boolean> result = userJpaDao.existsByApiKey("userdao004", "wrong_apikey");

            assertAll("result",
                    () -> assertTrue(result.isPresent()),
                    () -> assertFalse(result.get())
            );
        }

        @Test
        @DisplayName("existsByApiKey should return false when username doesn't match")
        void testExistsByApiKeyInvalidUsername() {
            createAndPersistUser("005", "userdao005", "UD00005E", "apikey_ud_005");

            Optional<Boolean> result = userJpaDao.existsByApiKey("wrong_user", "apikey_ud_005");

            assertAll("result",
                    () -> assertTrue(result.isPresent()),
                    () -> assertFalse(result.get())
            );
        }
    }

    @Nested
    class CountTests {

        @Test
        @DisplayName("count should return number of users")
        void testCount() {
            Long initialCount = userJpaDao.count();
            createAndPersistUser("006", "userdao006", "UD00006F", "apikey_ud_006");
            createAndPersistUser("007", "userdao007", "UD00007G", "apikey_ud_007");

            Long result = userJpaDao.count();

            assertEquals(initialCount + 2, result);
        }
    }

    @Nested
    class GetByIdTests {

        @Test
        @DisplayName("getById should return user when found")
        void testGetById() {
            UserJpaEntity user = createAndPersistUser("008", "userdao008", "UD00008H", "apikey_ud_008");

            Optional<UserJpaEntity> result = userJpaDao.getById(user.getUserId());

            assertAll("result",
                    () -> assertTrue(result.isPresent()),
                    () -> assertEquals(user.getUserId(), result.get().getUserId()),
                    () -> assertEquals("userdao008", result.get().getUsername())
            );
        }

        @Test
        @DisplayName("getById should return empty when not found")
        void testGetByIdNotFound() {
            Optional<UserJpaEntity> result = userJpaDao.getById(999999L);

            assertFalse(result.isPresent());
        }
    }
}
