package es.VetUp.banco_back.c_persistence.repository.mapper;

import es.VetUp.banco_back.b_domain.model.BankAccount;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankAccountJpaEntity;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.UserJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountPersistenceMapperTest {

    private final BankAccountPersistenceMapper mapper = BankAccountPersistenceMapper.getInstance();

    private UserJpaEntity createUserWithId(Long userId) {
        UserJpaEntity user = new UserJpaEntity();
        user.setUserId(userId);
        return user;
    }

    @Nested
    class FromBankAccountJpaEntityToBankAccountTests {

        @Test
        @DisplayName("should map JpaEntity to BankAccount correctly")
        void testMapping() {
            BankAccountJpaEntity jpaEntity = new BankAccountJpaEntity();
            jpaEntity.setAccountId(1L);
            jpaEntity.setIban("ES1234567890123456789012");
            jpaEntity.setBalance(new BigDecimal("2000.00"));
            jpaEntity.setUser(createUserWithId(200L));

            BankAccount result = mapper.fromBankAccountJpaEntityToBankAccount(jpaEntity);

            assertAll("result",
                    () -> assertEquals(1L, result.getAccountId()),
                    () -> assertEquals("ES1234567890123456789012", result.getIban()),
                    () -> assertEquals(new BigDecimal("2000.00"), result.getBalance()),
                    () -> assertEquals(200L, result.getUserId())
            );
        }

        @Test
        @DisplayName("should return null when input is null")
        void testNullInput() {
            BankAccount result = mapper.fromBankAccountJpaEntityToBankAccount(null);

            assertNull(result);
        }
    }

    @Nested
    class FromBankAccountToBankAccountJpaEntityTests {

        @Test
        @DisplayName("should map BankAccount to JpaEntity correctly")
        void testMapping() {
            BankAccount bankAccount = new BankAccount(1L, "ES1234567890123456789012", new BigDecimal("3000.00"), 300L);

            BankAccountJpaEntity result = mapper.fromBankAccountToBankAccountJpaEntity(bankAccount);

            assertAll("result",
                    () -> assertEquals(1L, result.getAccountId()),
                    () -> assertEquals("ES1234567890123456789012", result.getIban()),
                    () -> assertEquals(new BigDecimal("3000.00"), result.getBalance()),
                    () -> assertEquals(300L, result.getUser())
            );
        }

        @Test
        @DisplayName("should handle null userId")
        void testNullUserId() {
            BankAccount bankAccount = new BankAccount(1L, "ES1234567890123456789012", new BigDecimal("3000.00"), null);

            BankAccountJpaEntity result = mapper.fromBankAccountToBankAccountJpaEntity(bankAccount);

            assertAll("result",
                    () -> assertEquals(1L, result.getAccountId()),
                    () -> assertNull(result.getUser())
            );
        }

        @Test
        @DisplayName("should return null when input is null")
        void testNullInput() {
            BankAccountJpaEntity result = mapper.fromBankAccountToBankAccountJpaEntity(null);

            assertNull(result);
        }
    }

    @Test
    @DisplayName("getInstance should return singleton instance")
    void testSingleton() {
        BankAccountPersistenceMapper instance1 = BankAccountPersistenceMapper.getInstance();
        BankAccountPersistenceMapper instance2 = BankAccountPersistenceMapper.getInstance();

        assertSame(instance1, instance2);
    }
}
