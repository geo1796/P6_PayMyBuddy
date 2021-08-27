INSERT INTO User (user_id, password, email, balance, balance_currency_code)
VALUES (1, '$2a$12$7mx1cpN4PfBZ0BCAulbD/OknUSL.04TjPVTmysiTzW71w1sV5Tq3q', 'john.doe@mail.com', 0, 'EUR');

INSERT INTO User (user_id, password, email, balance, balance_currency_code)
VALUES (2, '$2a$12$Tq6O6Vr57KzLI/Ymur4uMev14I1XArW5CripfV/smg7qLWaDADoXq', 'son.goku@mail.com', 0, 'USD');

INSERT INTO Role (role_id, name)
VALUES (1, 'ROLE_USER');

INSERT INTO Role (role_id, name)
VALUES (2, 'ROLE_ADMIN');

INSERT INTO User_role (id, user_id, role_id)
VALUES (1, 1, 1);

INSERT INTO User_role (id, user_id, role_id)
VALUES (2, 2, 2);

INSERT INTO Transaction (id, amount, currency_code, fee, start_date, end_date, description, sender_id, receiver_id)
VALUES (1, 0,'EUR', 0, '2021-08-15', '2021-08-16', 'transaction test', 1, 2);

INSERT INTO User_contact (id, user_id, contact_id)
VALUES (1, 1, 2);

INSERT INTO Credit_card (card_number, first_name, last_name, expiration_date)
VALUES ('0000000000000000', 'John', 'Doe', '2025-01-01');

INSERT INTO User_credit_card (id, user_id, card_number)
VALUES (1, 1, '0000000000000000');

INSERT INTO Bank_account (iban, customer_id, password)
VALUES ('ibanTest', 123, 'passwordTest');

INSERT INTO User_bank_account (id, iban, user_id)
VALUES (1, 'ibanTest', 1);