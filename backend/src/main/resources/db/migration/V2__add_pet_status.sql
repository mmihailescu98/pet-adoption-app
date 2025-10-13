CREATE TYPE status_type AS ENUM ('PENDING', 'ADOPTED');

ALTER TABLE pets
    ADD COLUMN status status_type DEFAULT 'PENDING';

UPDATE pets SET status = 'PENDING' WHERE status IS NULL;