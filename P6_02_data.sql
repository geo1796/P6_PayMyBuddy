/*
SOURCE C:/Users/geoff/OneDrive/Bureau/OpenClassroom/P6/data.sql
*/

INSERT INTO User (user_id, password, email, balance, balance_currency_code) 
VALUES (1, '$2a$12$7mx1cpN4PfBZ0BCAulbD/OknUSL.04TjPVTmysiTzW71w1sV5Tq3q', 'john.doe@mail.com', 1000000000, 'EUR');

INSERT INTO User (user_id, password, email, balance, balance_currency_code) 
VALUES (2, '$2a$12$Tq6O6Vr57KzLI/Ymur4uMev14I1XArW5CripfV/smg7qLWaDADoXq', 'son.goku@mail.com', 0, 'USD');

INSERT INTO User (user_id, password, email, balance, balance_currency_code) 
VALUES (3, '$2a$12$I4jHVishFL8MVTbXic3xQ.SUgXI5fdYlkVDp8swnEkAPJ5UhHA.p6', 'son.gohan@mail.com', 0, 'USD');

INSERT INTO User (user_id, password, email, balance, balance_currency_code) 
VALUES (4, '$2a$12$w3kb5.krt/KsQAMETjJCN.E6kOGxqNq6QSazcvKldb/cHO18Urhlm', 'son.goten@mail.com', 0, 'USD');

INSERT INTO Role (role_id, name) 
VALUES (1, 'ROLE_USER');

INSERT INTO Role (role_id, name) 
VALUES (2, 'ROLE_ADMIN');

INSERT INTO User_role (id, user_id, role_id) 
VALUES (1, 1, 1);

INSERT INTO User_role (id, user_id, role_id) 
VALUES (2, 2, 2);

INSERT INTO User_contact (id, user_id, contact_id)
VALUES (1, 1, 2);

INSERT INTO Credit_card (card_number, first_name, last_name, expiration_date)
VALUES ('0000000000000000', 'John', 'Doe', '2025-01-01 00:00:00');

INSERT INTO User_credit_card (id, user_id, card_number)
VALUES (1, 1, '0000000000000000');

INSERT INTO Bank_account (iban, customer_id, password)
VALUES ('ibanJohnDoe', 123, 'passwordTest');

INSERT INTO User_bank_account (id, iban, user_id)
VALUES (1, 'ibanJohnDoe', 1);

