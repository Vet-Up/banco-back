-- ============================
-- ENUM TABLES
-- ============================

CREATE TABLE IF NOT EXISTS BankTransactionType (
    transaction_type_id BIGINT PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS BankTransactionOrigin (
    transaction_origin_id BIGINT PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

-- ============================
-- MAIN ENTITIES
-- ============================

CREATE TABLE IF NOT EXISTS Users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    first_name VARCHAR(50),
    last_name1 VARCHAR(50),
    last_name2 VARCHAR(50),
    dni VARCHAR(20) UNIQUE NOT NULL,
    api_key VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS BankAccount (
    account_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    balance DECIMAL(15,2) DEFAULT 0,
    iban VARCHAR(34) UNIQUE NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

CREATE TABLE IF NOT EXISTS CreditCard (
    source_card_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    card_number VARCHAR(20) UNIQUE NOT NULL,
    expiration_date DATE NOT NULL,
    cvc VARCHAR(4) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    account_id BIGINT NOT NULL,
    FOREIGN KEY (account_id) REFERENCES BankAccount(account_id)
);

CREATE TABLE IF NOT EXISTS BankTransaction (
    transaction_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    transaction_type_id BIGINT NOT NULL,
    transaction_origin_id BIGINT NOT NULL,
    source_card_id BIGINT,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    description VARCHAR(255),
    account_id BIGINT NOT NULL,

    FOREIGN KEY (transaction_type_id) REFERENCES BankTransactionType(transaction_type_id),
    FOREIGN KEY (transaction_origin_id) REFERENCES BankTransactionOrigin(transaction_origin_id),
    FOREIGN KEY (source_card_id) REFERENCES CreditCard(source_card_id),
    FOREIGN KEY (account_id) REFERENCES BankAccount(account_id)
);

-- ============================
-- INSERT INITIAL DATA FOR ENUM TABLES
-- ============================

INSERT INTO BankTransactionType (transaction_type_id, name) VALUES (1, 'Debit');
INSERT INTO BankTransactionType (transaction_type_id, name) VALUES (2, 'Credit');

INSERT INTO BankTransactionOrigin (transaction_origin_id, name) VALUES (1, 'Transfer');
INSERT INTO BankTransactionOrigin (transaction_origin_id, name) VALUES (2, 'DirectDebit');
INSERT INTO BankTransactionOrigin (transaction_origin_id, name) VALUES (3, 'BankCard');

