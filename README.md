# BankApplication_Backend

CUSTOMER TABLE :

CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    address VARCHAR(200),
    password VARCHAR(150),
    phone VARCHAR(10),
    email VARCHAR(150)
);

ACCOUNT TABLE :

CREATE TABLE accounts (
    id BIGSERIAL PRIMARY KEY,
    account_no BIGINT UNIQUE NOT NULL,
    account_type VARCHAR(255),
    account_holder_name VARCHAR(255),
    balance DOUBLE PRECISION,
    customer_id BIGINT NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

TRANSACTION TABLE :

CREATE TABLE transactions (
    transaction_id BIGSERIAL PRIMARY KEY,
    transaction_type VARCHAR(255) NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    transaction_date DATE NOT NULL,
    balance DOUBLE PRECISION NOT NULL,
    account_no BIGINT NOT NULL,
    FOREIGN KEY (account_no) REFERENCES accounts(account_no) ON DELETE CASCADE
);
