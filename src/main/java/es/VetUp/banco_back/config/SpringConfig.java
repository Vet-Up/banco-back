package es.VetUp.banco_back.config;


import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "es.VetUp.banco_back.c_persistence.dao.jpa")
@EntityScan(basePackages = "es.VetUp.banco_back.c_persistence.dao.jpa.entity")
public class SpringConfig {
}
