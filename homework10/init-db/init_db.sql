DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id int NOT NULL,
  name varchar(255) NOT NULL,
  balance bigint default 0 NOT NULL,
  created_at timestamp default NOW(),
  PRIMARY KEY (id)
);

INSERT INTO users (id, name, balance) VALUES (1, 'user1', 1000);
INSERT INTO users (id, name, balance) VALUES (2, 'user2', 2000);
INSERT INTO users (id, name, balance) VALUES (3, 'user3', 500);

