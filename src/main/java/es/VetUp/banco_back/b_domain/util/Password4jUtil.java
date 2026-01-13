package es.VetUp.banco_back.b_domain.util;

import com.password4j.Hash;
import com.password4j.Password;

public class Password4jUtil {
    public static String hashPassword(String password){

            Hash hash = Password.hash(password)
                    .addRandomSalt()
                    .withArgon2();

            return hash.getResult();
        }

        public static boolean verifyPassword(String plainPassword, String dbPassword) {
            return Password.check(plainPassword, dbPassword).withArgon2();
        }
    }

