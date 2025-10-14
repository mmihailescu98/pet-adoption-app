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

--Move old location to new table
INSERT INTO locations (city, state, street)
SELECT location, 'No state', 'No Street'
FROM pets
WHERE location IS NOT NULL;

UPDATE pets p
SET location_id = l.id
    FROM locations l
where l.city = p.location;

--Drop old location column
ALTER TABLE pets DROP COLUMN location;