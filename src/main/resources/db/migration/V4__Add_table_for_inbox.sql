CREATE TABLE "message" (
  id SERIAL PRIMARY KEY,
  sender VARCHAR(255),
  seq int,
  timestamp TIMESTAMP,
  content TEXT,
  threadId VARCHAR(225)
);

CREATE TABLE "thread" (
  threadId VARCHAR(225),
  status STATUS
);

CREATE TABLE "participant" (
  id SERIAL PRIMARY KEY,
  threadId VARCHAR(225),
  username VARCHAR(225)
);