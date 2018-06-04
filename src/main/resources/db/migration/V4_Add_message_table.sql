CREATE TABLE message (
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
  THREADID VARCHAR(225),
  username VARCHAR(225)
);