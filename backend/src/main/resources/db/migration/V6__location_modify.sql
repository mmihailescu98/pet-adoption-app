-- Add location table
CREATE TABLE locations(
                          id INT PRIMARY KEY  GENERATED ALWAYS AS IDENTITY,

                          street VARCHAR(255),
                          city VARCHAR(255) NOT NULL,
                          state VARCHAR(255) NOT NULL,

                          latitude DECIMAL(10,8),
                          longitude DECIMAL(11,8)
);

--Modify pets table
ALTER TABLE pets ADD COLUMN location_id INT;

ALTER TABLE pets
    ADD CONSTRAINT fk_pet_location
        FOREIGN KEY (location_id)
            REFERENCES locations(id);

INSERT INTO locations (street, city, state, latitude, longitude)
VALUES
    ('Bulevardul Unirii 1', 'București', 'Romania', 44.426767, 26.102538),
    ('Strada Memorandumului 10', 'Cluj-Napoca', 'Romania', 46.771210, 23.623635),
    ('Bulevardul Ștefan cel Mare și Sfânt 4', 'Iași', 'Romania', 47.158455, 27.601441);

WITH ordered_pets AS (
    SELECT id, ROW_NUMBER() OVER (ORDER BY id) AS rn
    FROM pets
),
     ordered_locations AS (
         SELECT id, ROW_NUMBER() OVER (ORDER BY id) AS rn
         FROM locations
     )
UPDATE pets p
SET location_id = l.id
FROM ordered_pets op
         JOIN ordered_locations l ON op.rn = l.rn
WHERE p.id = op.id;

--Drop old location column
ALTER TABLE pets DROP COLUMN location;