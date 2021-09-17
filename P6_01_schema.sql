/*
SOURCE C:/Users/geoff/OneDrive/Bureau/OpenClassroom/P6/schema.sql
*/

DROP DATABASE IF EXISTS PayMyBuddy;
CREATE DATABASE PayMyBuddy;
USE PayMyBuddy;

CREATE TABLE Bank_account (
                iban VARCHAR(34) NOT NULL,
                customer_id INT NOT NULL,
                password VARCHAR(100) NOT NULL,
                PRIMARY KEY (iban)
);


CREATE TABLE Role (
                role_id INT AUTO_INCREMENT NOT NULL,
                name VARCHAR(20) NOT NULL,
                PRIMARY KEY (role_id)
);


CREATE TABLE Credit_card (
                card_number CHAR(16) NOT NULL,
                first_name VARCHAR(30) NOT NULL,
                last_name VARCHAR(30) NOT NULL,
                expiration_date DATE NOT NULL,
                PRIMARY KEY (card_number)
);


CREATE TABLE User (
                user_id INT AUTO_INCREMENT NOT NULL,
                password VARCHAR(255) NOT NULL,
                email VARCHAR(50) NOT NULL,
                balance DOUBLE PRECISION DEFAULT 0 NOT NULL,
                balance_currency_code CHAR(3) DEFAULT 'EUR' NOT NULL,
                PRIMARY KEY (user_id)
);


CREATE TABLE Credit_card_transaction (
                id INT AUTO_INCREMENT NOT NULL,
                user_id INT NOT NULL,
                card_number CHAR(16) NOT NULL,
                amount DOUBLE PRECISION NOT NULL,
                currency_code CHAR(3) NOT NULL,
                PRIMARY KEY (id)
);


CREATE TABLE Bank_account_transaction (
                id INT AUTO_INCREMENT NOT NULL,
                user_id INT NOT NULL,
                iban VARCHAR(34) NOT NULL,
                amount DOUBLE PRECISION NOT NULL,
                currency_code CHAR(3) NOT NULL,
                to_balance BOOLEAN NOT NULL,
                PRIMARY KEY (id)
);


CREATE TABLE User_contact (
                id INT AUTO_INCREMENT NOT NULL,
                user_id INT NOT NULL,
                contact_id INT NOT NULL,
                PRIMARY KEY (id)
);


CREATE TABLE User_bank_account (
                id INT AUTO_INCREMENT NOT NULL,
                iban VARCHAR(34) NOT NULL,
                user_id INT NOT NULL,
                PRIMARY KEY (id)
);


CREATE TABLE User_credit_card (
                id INT AUTO_INCREMENT NOT NULL,
                user_id INT NOT NULL,
                card_number CHAR(16) NOT NULL,
                PRIMARY KEY (id)
);


CREATE TABLE Transaction (
                id INT AUTO_INCREMENT NOT NULL,
                amount DOUBLE PRECISION NOT NULL,
                currency_code CHAR(3) NOT NULL,
                fee DOUBLE PRECISION DEFAULT 0 NOT NULL,
                start_date DATETIME NOT NULL,
                end_date DATETIME,
                description VARCHAR(100),
                sender_id INT NOT NULL,
                receiver_id INT NOT NULL,
                PRIMARY KEY (id)
);


CREATE TABLE User_role (
                id INT AUTO_INCREMENT NOT NULL,
                user_id INT NOT NULL,
                role_id INT NOT NULL,
                PRIMARY KEY (id)
);


ALTER TABLE User_bank_account ADD CONSTRAINT bank_account_user_bank_account_fk
FOREIGN KEY (iban)
REFERENCES Bank_account (iban)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Bank_account_transaction ADD CONSTRAINT bank_account_bank_account_transaction_fk
FOREIGN KEY (iban)
REFERENCES Bank_account (iban)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE User_role ADD CONSTRAINT role_user_role_fk
FOREIGN KEY (role_id)
REFERENCES Role (role_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE User_credit_card ADD CONSTRAINT credit_card_user_credit_card_fk
FOREIGN KEY (card_number)
REFERENCES Credit_card (card_number)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Credit_card_transaction ADD CONSTRAINT credit_card_credit_card_transaction_fk
FOREIGN KEY (card_number)
REFERENCES Credit_card (card_number)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE User_role ADD CONSTRAINT user_user_role_fk
FOREIGN KEY (user_id)
REFERENCES User (user_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Transaction ADD CONSTRAINT user_transaction_fk
FOREIGN KEY (sender_id)
REFERENCES User (user_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Transaction ADD CONSTRAINT user_transaction_fk1
FOREIGN KEY (receiver_id)
REFERENCES User (user_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE User_credit_card ADD CONSTRAINT user_user_credit_card_fk
FOREIGN KEY (user_id)
REFERENCES User (user_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE User_bank_account ADD CONSTRAINT user_user_bank_account_fk
FOREIGN KEY (user_id)
REFERENCES User (user_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE User_contact ADD CONSTRAINT user_user_contact_fk
FOREIGN KEY (user_id)
REFERENCES User (user_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE User_contact ADD CONSTRAINT user_user_contact_fk1
FOREIGN KEY (contact_id)
REFERENCES User (user_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Bank_account_transaction ADD CONSTRAINT user_bank_account_transaction_fk
FOREIGN KEY (user_id)
REFERENCES User (user_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Credit_card_transaction ADD CONSTRAINT user_credit_card_transaction_fk
FOREIGN KEY (user_id)
REFERENCES User (user_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;