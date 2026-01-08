package es.VetUp.banco_back.c_persistence.dao.jpa.entity;

import jakarta.persistence.*;
import org.checkerframework.checker.units.qual.C;

import java.io.Serializable;

@Entity
@Table(name = "Users")
public class UserJpaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
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
}
