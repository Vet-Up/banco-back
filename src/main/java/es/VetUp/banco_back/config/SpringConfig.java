package es.VetUp.banco_back.config;


import es.VetUp.banco_back.b_domain.repository.BankTransactionRepository;
import es.VetUp.banco_back.b_domain.repository.CreditCardRepository;
import es.VetUp.banco_back.b_domain.repository.UserRepository;
import es.VetUp.banco_back.b_domain.service.BankTransactionService;
import es.VetUp.banco_back.b_domain.service.CreditCardService;
import es.VetUp.banco_back.b_domain.service.UserService;
import es.VetUp.banco_back.b_domain.service.impl.BankTransactionServiceImpl;
import es.VetUp.banco_back.b_domain.service.impl.CreditCardServiceImpl;
import es.VetUp.banco_back.b_domain.service.impl.UserServiceImpl;
import es.VetUp.banco_back.c_persistence.dao.jpa.BankTransactionJpaDao;
import es.VetUp.banco_back.c_persistence.dao.jpa.CreditCardJpaDao;
import es.VetUp.banco_back.c_persistence.dao.jpa.UserJpaDao;
import es.VetUp.banco_back.c_persistence.dao.jpa.impl.BankTransactionJpaDaoImpl;
import es.VetUp.banco_back.c_persistence.dao.jpa.impl.CreditCardJpaDaoImpl;
import es.VetUp.banco_back.c_persistence.dao.jpa.impl.UserJpaDaoImpl;
import es.VetUp.banco_back.c_persistence.repository.BankTransactionRepositoryImpl;
import es.VetUp.banco_back.c_persistence.repository.CreditCardRepositoryImpl;
import es.VetUp.banco_back.c_persistence.repository.UserRepositoryImpl;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "es.VetUp.banco_back.c_persistence.dao.jpa")
@EntityScan(basePackages = "es.VetUp.banco_back.c_persistence.dao.jpa.entity")
public class SpringConfig {

    @Bean
    public UserJpaDao userJpaDao() {return new UserJpaDaoImpl();
    }

    @Bean
    public UserRepository userRepository(UserJpaDao userJpaDao) {return new UserRepositoryImpl(userJpaDao);
    }

    @Bean
    public UserService userService(UserRepository userRepository) {return new UserServiceImpl(userRepository);
    }

    @Bean
    public CreditCardJpaDao creditCardJpaDao() {return new CreditCardJpaDaoImpl();
    }

    @Bean
    public CreditCardRepository creditCardRepository(CreditCardJpaDao creditCardJpaDao) {return new CreditCardRepositoryImpl(creditCardJpaDao);
    }

    @Bean
    public CreditCardService creditCardService(CreditCardRepository creditCardRepository) {return new CreditCardServiceImpl(creditCardRepository);
    }

    @Bean
    public BankTransactionJpaDao bankTransactionJpaDao() {return new BankTransactionJpaDaoImpl();
    }

    @Bean
    public BankTransactionRepository bankTransactionRepository(BankTransactionJpaDao bankTransactionJpaDao) {return new BankTransactionRepositoryImpl(bankTransactionJpaDao);
    }

    @Bean
    public BankTransactionService bankTransactionService(BankTransactionRepository bankTransactionRepository) {return new BankTransactionServiceImpl(bankTransactionRepository);
    }


}
