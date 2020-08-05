
CREATE TABLE organisations
(id VARCHAR(20) PRIMARY KEY,
 name VARCHAR(30),
 address_line_1 VARCHAR(30),
 address_line_2 VARCHAR(30),
 city VARCHAR(30),
 county VARCHAR(30),
 postcode VARCHAR(9),
 country VARCHAR(30),
 telephone VARCHAR(30),
 email VARCHAR(30),

 creation_ts TIMESTAMP,
 update_ts TIMESTAMP);
