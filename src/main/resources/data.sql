-- Пользователь 1
INSERT INTO users (id, name, date_of_birth, password) VALUES
    (1, 'Ivan Testov', '1990-01-01', '$2a$10$6kmXQ7c4u1GzBnMGL9sIwe8c0IacMKxUyEgtTDRyMT9bjs0uEtXWq'); -- password123

INSERT INTO phone_data (id, user_id, phone) VALUES
    (1, 1, '79201234567');

INSERT INTO email_data (id, user_id, email) VALUES
    (1, 1, 'test@example.com');

INSERT INTO account (id, user_id, balance, initial_balance) VALUES
    (1, 1, 1000.00, 1000.00);


-- Пользователь 2
INSERT INTO users (id, name, date_of_birth, password) VALUES
    (2, 'Petr Second', '1991-02-02', '$2a$10$6kmXQ7c4u1GzBnMGL9sIwe8c0IacMKxUyEgtTDRyMT9bjs0uEtXWq'); -- password123

INSERT INTO phone_data (id, user_id, phone) VALUES
    (2, 2, '79201112233');

INSERT INTO email_data (id, user_id, email) VALUES
    (2, 2, 'second@example.com');

INSERT INTO account (id, user_id, balance, initial_balance) VALUES
    (2, 2, 500.00, 500.00);
