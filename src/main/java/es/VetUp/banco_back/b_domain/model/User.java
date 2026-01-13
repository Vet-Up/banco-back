package es.VetUp.banco_back.b_domain.model;

import java.util.Objects;

public class User {
    private final Long userId;
    private final String username;
    private final String password;
    private final String name;
    private final String firstSurname;
    private final String secondSurname;
    private final String dni;
    private final String apiKey;

    public User(Long userId, String username, String password, String name, String firstSurname, String secondSurname, String dni, String apiKey) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.name = name;
        this.firstSurname = firstSurname;
        this.secondSurname = secondSurname;
        this.dni = dni;
        this.apiKey = apiKey;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getFirstSurname() {
        return firstSurname;
    }

    public String getSecondSurname() {
        return secondSurname;
    }

    public String getDni() {
        return dni;
    }

    public String getApiKey() {
        return apiKey;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(name, user.name) && Objects.equals(firstSurname, user.firstSurname) && Objects.equals(secondSurname, user.secondSurname) && Objects.equals(dni, user.dni);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, password, name, firstSurname, secondSurname, dni);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", firstSurname='" + firstSurname + '\'' +
                ", secondSurname='" + secondSurname + '\'' +
                ", dni='" + dni + '\'' +
                ", apiKey='" + apiKey + '\'' +
                '}';
    }
}
