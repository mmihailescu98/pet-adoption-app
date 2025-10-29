-- V4__add_favorite_pets_table.sql

CREATE TABLE favorite_pets (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id BIGINT NOT NULL,
    pet_id BIGINT NOT NULL,
    CONSTRAINT fk_favorite_pets_user FOREIGN KEY (user_id)
       REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_favorite_pets_pet FOREIGN KEY (pet_id)
       REFERENCES pets(id) ON DELETE CASCADE,
    CONSTRAINT uc_favorite_pets_user_pet UNIQUE (user_id, pet_id)
);

INSERT INTO favorite_pets (user_id, pet_id)
VALUES
    (1, 1),
    (1,51);

