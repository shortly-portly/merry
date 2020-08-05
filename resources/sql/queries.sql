-- :name create-user! :! :n
-- :doc creates a new user record
INSERT INTO users
(id, first_name, last_name, email, pass)
VALUES (:id, :first_name, :last_name, :email, :pass)

-- :name update-user! :! :n
-- :doc updates an existing user record
UPDATE users
SET first_name = :first_name, last_name = :last_name, email = :email
WHERE id = :id

-- :name get-user :? :1
-- :doc retrieves a user record given the id
SELECT * FROM users
WHERE id = :id

-- :name delete-user! :! :n
-- :doc deletes a user record given the id
DELETE FROM users
WHERE id = :id

-- :name create-organisation! :! :n
-- :doc creates a new organisation record
INSERT INTO organisations
(id, name, adress_line_1, address_line_2, city, county, postcode, country, telephone, email)
VALUES (:id, :name, :address_line_1, :address_line_2, :city, :county, :postcode, :country, :telephone, :email)


-- :name get-organisations :! n
-- :doc gets a list of organisations
SELECT * FROM organisations

-- :name update-organisation! :! :n
-- :doc updates an existing organisation record
UPDATE organisations
SET name = :name,
address_line_1 = :address_line_1,
address_line_2 = :address_line_2,
city = :city,
county = :county,
postcode = :postcode,
country = :country,
telephone = :telephone,
email = :email
WHERE id = :id

-- :name get-organisation :? :1
-- :doc retrieves a organisation record given the id
SELECT * FROM organisations
WHERE id = :id

-- :name delete-organisation! :! :n
-- :doc deletes a organisation record given the id
DELETE FROM organisations
WHERE id = :id
