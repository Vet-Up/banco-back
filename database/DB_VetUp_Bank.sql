-- ============================
-- ENUM TABLES
-- ============================

CREATE TABLE BankTransactionType (
    transaction_type_id INT PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

CREATE TABLE BankTransactionOrigin (
    transaction_origin_id INT PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

-- ============================
-- MAIN ENTITIES
-- ============================

CREATE TABLE Users (
    id_user SERIAL PRIMARY KEY,
    login VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    first_name VARCHAR(50),
    last_name1 VARCHAR(50),
    last_name2 VARCHAR(50),
    dni VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE BankAccount (
    account_id SERIAL PRIMARY KEY,
    balance DECIMAL(15,2) DEFAULT 0,
    iban VARCHAR(34) UNIQUE NOT NULL,
    id_user INT NOT NULL,
    FOREIGN KEY (id_user) REFERENCES Users(id_user)
);

CREATE TABLE CreditCard (
    source_card_id SERIAL PRIMARY KEY,
    card_number VARCHAR(20) UNIQUE NOT NULL,
    expiration_date DATE NOT NULL,
    cvc VARCHAR(4) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    account_id INT NOT NULL,
    FOREIGN KEY (account_id) REFERENCES BankAccount(account_id)
);

CREATE TABLE BankTransaction (
    id SERIAL PRIMARY KEY,
    transaction_type_id INT NOT NULL,
    transaction_origin_id INT NOT NULL,
    source_card_id INT,
    transaction_date DATE NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    description VARCHAR(255),
    account_id INT NOT NULL,

    FOREIGN KEY (transaction_type_id) REFERENCES BankTransactionType(transaction_type_id),
    FOREIGN KEY (transaction_origin_id) REFERENCES BankTransactionOrigin(transaction_origin_id),
    FOREIGN KEY (source_card_id) REFERENCES CreditCard(source_card_id),
    FOREIGN KEY (account_id) REFERENCES BankAccount(account_id)
);
