package es.VetUp.banco_back.c_persistence;

import es.VetUp.banco_back.c_persistence.dao.jpa.BankTransactionJpaDao;
import es.VetUp.banco_back.c_persistence.dao.jpa.CreditCardJpaDao;
import es.VetUp.banco_back.c_persistence.dao.jpa.impl.BankTransactionJpaDaoImpl;
import es.VetUp.banco_back.c_persistence.dao.jpa.impl.CreditCardJpaDaoImpl;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "es.VetUp.banco_back.c_persistence.dao.jpa")
@EntityScan(basePackages = "es.VetUp.banco_back.c_persistence.dao.jpa.entity")
public class TestConfig {

    @Bean
    public BankTransactionJpaDao bankTransactionJpaDao() {
        return new BankTransactionJpaDaoImpl();
    }

    @Bean
    public CreditCardJpaDao creditCardJpaDao() {
        return new CreditCardJpaDaoImpl();
    }

}
