ALTER TABLE situation DROP CONSTRAINT situation_pkey;
ALTER TABLE situation RENAME COLUMN "id" TO "situation";
ALTER TABLE situation ADD id SERIAL PRIMARY KEY;