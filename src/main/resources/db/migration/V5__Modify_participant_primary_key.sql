ALTER TABLE participant DROP CONSTRAINT participant_pkey;

ALTER TABLE participant DROP COLUMN "id";

ALTER TABLE participant ADD PRIMARY KEY (threadid, username);