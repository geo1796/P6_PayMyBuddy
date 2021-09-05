DROP TABLE IF EXISTS User_contact;
CREATE TABLE User_contact (
                id IDENTITY NOT NULL,
                user_id INTEGER NOT NULL,
                contact_id INTEGER NOT NULL,
                CONSTRAINT User_contact_pk PRIMARY KEY (id)
);

DROP TABLE IF EXISTS User_bank_account;
CREATE TABLE User_bank_account (
                id IDENTITY NOT NULL,
                iban VARCHAR(34) NOT NULL,
                user_id INTEGER NOT NULL,
                CONSTRAINT User_bank_account_pk PRIMARY KEY (id)
);

DROP TABLE IF EXISTS User_credit_card;
CREATE TABLE User_credit_card (
                id IDENTITY NOT NULL,
                user_id INTEGER NOT NULL,
                card_number CHAR(16) NOT NULL,
                CONSTRAINT User_credit_card_pk PRIMARY KEY (id)
);

DROP TABLE IF EXISTS User_role;
CREATE TABLE User_role (
                id INT AUTO_INCREMENT NOT NULL,
                user_id INT NOT NULL,
                role_id INT NOT NULL,
                PRIMARY KEY (id)
);


ALTER TABLE User_bank_account ADD CONSTRAINT Bank_account_User_bank_account_fk
FOREIGN KEY (iban)
REFERENCES Bank_account (iban)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE User_role ADD CONSTRAINT Role_User_role_fk
FOREIGN KEY (role_id)
REFERENCES Role (role_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE User_credit_card ADD CONSTRAINT Credit_card_User_credit_card_fk
FOREIGN KEY (card_number)
REFERENCES Credit_card (card_number)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE User_role ADD CONSTRAINT User_User_role_fk
FOREIGN KEY (user_id)
REFERENCES User (user_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Transaction ADD CONSTRAINT User_Transaction_fk
FOREIGN KEY (sender_id)
REFERENCES User (user_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Transaction ADD CONSTRAINT User_Transaction_fk1
FOREIGN KEY (receiver_id)
REFERENCES User (user_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE User_credit_card ADD CONSTRAINT User_User_credit_card_fk
FOREIGN KEY (user_id)
REFERENCES User (user_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE User_bank_account ADD CONSTRAINT User_User_bank_account_fk
FOREIGN KEY (user_id)
REFERENCES User (user_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE User_contact ADD CONSTRAINT User_User_contact_fk
FOREIGN KEY (user_id)
REFERENCES User (user_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE User_contact ADD CONSTRAINT User_User_contact_fk1
FOREIGN KEY (contact_id)
REFERENCES User (user_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;
