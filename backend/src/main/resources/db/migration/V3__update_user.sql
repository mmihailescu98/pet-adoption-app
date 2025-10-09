ALTER TABLE users
    ADD COLUMN name VARCHAR(255),
    ADD COLUMN phone VARCHAR(255) UNIQUE,
    ADD COLUMN email VARCHAR(255) UNIQUE,
    ADD COLUMN location VARCHAR(255),
    ADD COLUMN imgURL VARCHAR(255),
    ADD COLUMN bio VARCHAR(1000);

INSERT INTO users (username, password, name, phone, email, location, imgURL, bio)
VALUES ('username', 'password', 'user', '0987654321', 'user@example.com', 'Bucharest', 'https://media.istockphoto.com/id/1337144146/vector/default-avatar-profile-icon-vector.jpg?s=612x612&w=0&k=20&c=BIbFwuv7FxTWvh5S3vB6bkT0Qv8Vn8N5Ffseq84ClGI=', 'Basic user test account.');

INSERT INTO user_roles (user_id, role)
VALUES ((SELECT id FROM users WHERE username = 'username'), 'USER');
