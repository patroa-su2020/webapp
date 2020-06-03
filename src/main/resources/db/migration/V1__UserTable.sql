-- CREATE TABLE user_data (
--     email VARCHAR(100) NOT NULL PRIMARY KEY ,
--     password VARCHAR(100) NOT NULL,
--     first_name VARCHAR(100) NOT NULL,
--     last_name VARCHAR(100) NOT NULL
-- );
--
-- CREATE TABLE sruti(
-- name VARCHAR (100) PRIMARY KEY
-- );

-- CREATE TABLE book_data (
--     book_id VARCHAR(100) NOT NULL PRIMARY KEY ,
--     isbn VARCHAR(100) NOT NULL,
--     title VARCHAR(100) NOT NULL,
--     authors VARCHAR(100) NOT NULL,
--     publication_date DATE NOT NULL,
--     quantity VARCHAR(100) NOT NULL,
--     price NUMERIC NOT NULL,
--     create_dt_tm NUMERIC ,
--     update_dt_tm NUMERIC ,
--     seller_name VARCHAR(100) ,
--     seller_id VARCHAR(100)
--
-- );

CREATE TABLE cart_data (
cart_id VARCHAR(100) NOT NULL PRIMARY KEY,
book_id VARCHAR(100) NOT NULL,
buyer_id VARCHAR(100) NOT NULL,
quantity NUMERIC
);