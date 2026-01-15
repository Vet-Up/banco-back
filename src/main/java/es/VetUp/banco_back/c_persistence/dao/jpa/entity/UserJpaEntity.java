package es.VetUp.banco_back.c_persistence.dao.jpa.entity;

import jakarta.persistence.*;
import org.checkerframework.checker.units.qual.C;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "Users")
public class UserJpaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false, unique = true)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String name;

    @Column(name = "last_name1", nullable = false)
    private String firstSurname;

    @Column(name = "last_name2", nullable = false)
    private String secondSurname;

    @Column(name = "dni", nullable = false, unique = true)
    private String dni;

    @Column(name = "api_key", nullable = false, unique = true)
    private String apiKey;


    public UserJpaEntity() {
    }

    public UserJpaEntity(Long userId, String username, String password, String name, String firstSurname, String secondSurname, String dni, String apiKey) {
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

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFirstSurname(String firstSurname) {
        this.firstSurname = firstSurname;
    }

    public void setSecondSurname(String secondSurname) {
        this.secondSurname = secondSurname;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserJpaEntity that = (UserJpaEntity) o;
        return Objects.equals(userId, that.userId) && Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(name, that.name) && Objects.equals(firstSurname, that.firstSurname) && Objects.equals(secondSurname, that.secondSurname) && Objects.equals(dni, that.dni) && Objects.equals(apiKey, that.apiKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, password, name, firstSurname, secondSurname, dni, apiKey);
    }
}
