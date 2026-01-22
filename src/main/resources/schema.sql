DROP TABLE IF EXISTS product_ingredients;
DROP TABLE IF EXISTS product_extras;
DROP TABLE IF EXISTS extras;
DROP TABLE IF EXISTS ingredients;
DROP TABLE IF EXISTS products;

DROP SEQUENCE IF EXISTS ingredient_seq;
DROP SEQUENCE IF EXISTS product_seq;
DROP SEQUENCE IF EXISTS extra_seq;

CREATE SEQUENCE ingredient_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE product_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE extra_seq START WITH 1 INCREMENT BY 1;


CREATE TABLE ingredients (
                             id SERIAL PRIMARY KEY,
                             name VARCHAR(50) NOT NULL
);

CREATE TABLE extras (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(50) NOT NULL,
                        price_cents INTEGER NOT NULL CHECK (price_cents >= 0)
);

CREATE TABLE products (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(50) NOT NULL,
                          price_cents INTEGER NOT NULL CHECK (price_cents >= 0)
);

CREATE TABLE product_ingredients (
                                     id SERIAL PRIMARY KEY,
                                     product_id INTEGER NOT NULL REFERENCES products(id),
                                     ingredient_id INTEGER NOT NULL REFERENCES ingredients(id),
                                     amount INTEGER NOT NULL
);

CREATE TABLE product_extras (
                                id SERIAL PRIMARY KEY,
                                product_id INTEGER NOT NULL REFERENCES products(id),
                                extra_id INTEGER NOT NULL REFERENCES extras(id),
                                amount INTEGER NOT NULL
);
