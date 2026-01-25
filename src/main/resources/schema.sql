DROP TABLE IF EXISTS order_item_extras;
DROP TABLE IF EXISTS order_item_ingredients;
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;

DROP TABLE IF EXISTS product_ingredients;
DROP TABLE IF EXISTS product_extras;
DROP TABLE IF EXISTS extras;
DROP TABLE IF EXISTS ingredients;
DROP TABLE IF EXISTS products;

DROP SEQUENCE IF EXISTS ingredient_seq;
DROP SEQUENCE IF EXISTS product_seq;
DROP SEQUENCE IF EXISTS extra_seq;
DROP SEQUENCE IF EXISTS order_seq;
DROP SEQUENCE IF EXISTS order_item_seq;
DROP SEQUENCE IF EXISTS order_item_ingredient_seq;
DROP SEQUENCE IF EXISTS order_item_extra_seq;

CREATE SEQUENCE ingredient_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE product_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE extra_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE order_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE order_item_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE order_item_ingredient_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE order_item_extra_seq START WITH 1 INCREMENT BY 1;


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
                                     ingredient_id INTEGER NOT NULL REFERENCES ingredients(id)
);

CREATE TABLE product_extras (
                                id SERIAL PRIMARY KEY,
                                product_id INTEGER NOT NULL REFERENCES products(id),
                                extra_id INTEGER NOT NULL REFERENCES extras(id)
);


CREATE TABLE orders (
                        id SERIAL PRIMARY KEY,
                        order_number VARCHAR(20) NOT NULL UNIQUE,
                        created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                        total_price_cents INTEGER NOT NULL CHECK (total_price_cents >= 0)
);

CREATE TABLE order_items (
                             id SERIAL PRIMARY KEY,
                             order_id INTEGER NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
                             product_name VARCHAR(100) NOT NULL,
                             base_price_cents INTEGER NOT NULL CHECK (base_price_cents >= 0),
                             quantity INTEGER NOT NULL CHECK (quantity > 0)
);

CREATE TABLE order_item_ingredients (
                                        id SERIAL PRIMARY KEY,
                                        order_item_id INTEGER NOT NULL REFERENCES order_items(id) ON DELETE CASCADE,
                                        ingredient_name VARCHAR(50) NOT NULL
);

CREATE TABLE order_item_extras (
                                   id SERIAL PRIMARY KEY,
                                   order_item_id INTEGER NOT NULL REFERENCES order_items(id) ON DELETE CASCADE,
                                   extra_name VARCHAR(50) NOT NULL,
                                   price_cents INTEGER NOT NULL CHECK (price_cents >= 0)
);
