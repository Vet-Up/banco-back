package es.VetUp.banco_back.b_domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("Test User Creation")
    void testUserCreation() {
        Long userId = 1L;
        String username = "johndoe";
        String password = "password123";
        String name = "John";
        String firstSurname = "Doe";
        String secondSurname = "Smith";
        String dni = "12345678A";
        String apiKey = "api_key_123";

        User user = assertDoesNotThrow(() -> new User(userId, username, password, name, firstSurname, secondSurname, dni, apiKey));

        assertAll("user",
                () -> assertEquals(userId, user.getUserId()),
                () -> assertEquals(username, user.getUsername()),
                () -> assertEquals(password, user.getPassword()),
                () -> assertEquals(name, user.getName()),
                () -> assertEquals(firstSurname, user.getFirstSurname()),
                () -> assertEquals(secondSurname, user.getSecondSurname()),
                () -> assertEquals(dni, user.getDni()),
                () -> assertEquals(apiKey, user.getApiKey())
        );
    }

    @Test
    @DisplayName("Test User Creation with different data")
    void testUserCreationDifferentData() {
        Long userId = 2L;
        String username = "janesmith";
        String password = "securepass456";
        String name = "Jane";
        String firstSurname = "Smith";
        String secondSurname = "Roberts";
        String dni = "87654321B";
        String apiKey = "api_key_456";

        User user = assertDoesNotThrow(() -> new User(userId, username, password, name, firstSurname, secondSurname, dni, apiKey));

        assertAll("user",
                () -> assertEquals(userId, user.getUserId()),
                () -> assertEquals(username, user.getUsername()),
                () -> assertEquals(password, user.getPassword()),
                () -> assertEquals(name, user.getName()),
                () -> assertEquals(firstSurname, user.getFirstSurname()),
                () -> assertEquals(secondSurname, user.getSecondSurname()),
                () -> assertEquals(dni, user.getDni()),
                () -> assertEquals(apiKey, user.getApiKey())
        );
    }

    @Test
    @DisplayName("Test User Equals - Same object")
    void testUserEqualsSameObject() {
        User user = new User(1L, "johndoe", "password123", "John", "Doe", "Smith", "12345678A", "api_key_123");

        assertEquals(user, user);
    }

    @Test
    @DisplayName("Test User Equals - Equal objects")
    void testUserEqualsEqualObjects() {
        User user1 = new User(1L, "johndoe", "password123", "John", "Doe", "Smith", "12345678A", "api_key_123");
        User user2 = new User(1L, "johndoe", "password123", "John", "Doe", "Smith", "12345678A", "api_key_123");

        assertEquals(user1, user2);
    }

    @Test
    @DisplayName("Test User Equals - Different objects")
    void testUserEqualsDifferentObjects() {
        User user1 = new User(1L, "johndoe", "password123", "John", "Doe", "Smith", "12345678A", "api_key_123");
        User user2 = new User(2L, "janesmith", "password456", "Jane", "Smith", "Roberts", "87654321B", "api_key_456");

        assertNotEquals(user1, user2);
    }

    @Test
    @DisplayName("Test User Equals - Null comparison")
    void testUserEqualsNull() {
        User user = new User(1L, "johndoe", "password123", "John", "Doe", "Smith", "12345678A", "api_key_123");

        assertNotEquals(null, user);
    }

    @Test
    @DisplayName("Test User HashCode - Equal objects have same hashCode")
    void testUserHashCodeEqual() {
        User user1 = new User(1L, "johndoe", "password123", "John", "Doe", "Smith", "12345678A", "api_key_123");
        User user2 = new User(1L, "johndoe", "password123", "John", "Doe", "Smith", "12345678A", "api_key_123");

        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    @DisplayName("Test User ToString")
    void testUserToString() {
        User user = new User(1L, "johndoe", "password123", "John", "Doe", "Smith", "12345678A", "api_key_123");

        String result = user.toString();

        assertTrue(result.contains("userId=1"));
        assertTrue(result.contains("username='johndoe'"));
        assertTrue(result.contains("name='John'"));
        assertTrue(result.contains("dni='12345678A'"));
    }
}
