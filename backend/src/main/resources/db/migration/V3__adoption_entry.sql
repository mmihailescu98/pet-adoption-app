CREATE TABLE adoptions (
   id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
   pet_id BIGINT NOT NULL,
   publisher_id BIGINT,
   additional_images TEXT[],
   contact_number VARCHAR(255),
   adopter_id BIGINT,
   created_at TIMESTAMP,
   adopted_at TIMESTAMP,

   CONSTRAINT fk_pet
       FOREIGN KEY (pet_id) REFERENCES pets(id)
           ON DELETE CASCADE,

   CONSTRAINT fk_publisher
       FOREIGN KEY (publisher_id) REFERENCES users(id)
           ON DELETE CASCADE,

   CONSTRAINT fk_adopter
       FOREIGN KEY (adopter_id) REFERENCES users(id)
           ON DELETE SET NULL
);

-- Insert adoption listings -- yes they were generated with chatgpt
-- all adoptions do not have an adopter , if needed for testing it can be added later
INSERT INTO adoptions (
    pet_id, publisher_id, additional_images, contact_number, adopter_id, created_at, adopted_at
)
VALUES
    (
        1, -- Buddy (Dog)
        1, -- Publisher: DariusSopom
        ARRAY['https://example.com/buddy1.jpg', 'https://example.com/buddy2.jpg'],
        '555-123-4567',
        NULL,
        NOW(),
        NULL
    ),
    (
        51, -- Mittens (Cat)
        1, -- Publisher: DariusSopom
        ARRAY['https://example.com/mittens1.jpg'],
        '555-987-6543',
        NULL,
        NOW(),
        NULL
    ),
    (
        101, -- Goldie (Fish)
        1, -- Publisher: DariusSopom
        ARRAY['https://example.com/goldie1.jpg', 'https://example.com/goldie2.jpg'],
        '555-555-1212',
        NULL,
        NOW(),
        NULL
    );



