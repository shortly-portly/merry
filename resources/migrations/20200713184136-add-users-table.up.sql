CREATE TABLE users
(id VARCHAR(20) PRIMARY KEY,
 first_name VARCHAR(30),
 last_name VARCHAR(30),
 email VARCHAR(30),
 password VARCHAR(300),
 last_login TIMESTAMP,
 is_active BOOLEAN);
