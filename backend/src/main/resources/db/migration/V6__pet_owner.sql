ALTER TABLE pets
ADD COLUMN owner_id BIGINT NOT NULL DEFAULT 1;

ALTER TABLE pets
ADD CONSTRAINT fk_pet_owner
FOREIGN KEY (owner_id) REFERENCES users(id);

--password is user123
UPDATE users SET password = '$2a$10$XiPHjHZ3LhhkfGochlQwEuzfOX..ojMMYPFCQttddEM7708cTpVtK' WHERE username = 'DariusSopom'

