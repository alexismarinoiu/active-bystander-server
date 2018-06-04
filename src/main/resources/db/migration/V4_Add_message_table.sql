CREATE TABLE message (
  id SERIAL PRIMARY KEY,
  sender VARCHAR(255),
  seq int, timestamp TIMESTAMP,
  content TEXT,
  threadId VARCHAR(225)
);

CREATE TABLE thread (
  THREADID VARCHAR(225),
  STATUS VARCHAR(16)
);

CREATE TABLE participant (
  id SERIAL PRIMARY KEY,
  THREADID VARCHAR(225),
  username VARCHAR(225)
);