DROP TABLE IF EXISTS extras;
DROP TABLE IF EXISTS ingredients;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS product_ingredients;
DROP TABLE IF EXISTS product_extras;

DROP SEQUENCE IF EXISTS ingredient_seq;
DROP SEQUENCE IF EXISTS product_seq;
DROP SEQUENCE IF EXISTS extra_seq;

CREATE SEQUENCE ingredient_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE product_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE extra_seq START WITH 1 INCREMENT BY 1;


CREATE TABLE ingredients (
    id INTEGER DEFAULT NEXT VALUE FOR ingredient_seq NOT NULL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE extras (
    id INTEGER DEFAULT NEXT VALUE FOR extra_seq NOT NULL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    price_cents INTEGER NOT NULL CHECK (price_cents >= 0)
);


CREATE TABLE products (
    id INTEGER DEFAULT NEXT VALUE FOR product_seq NOT NULL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    price_cents INTEGER NOT NULL CHECK (price_cents >= 0)
);

CREATE TABLE product_ingredients (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_id INTEGER NOT NULL,
    ingredient_id INTEGER NOT NULL,
    amount INTEGER NOT NULL,

    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (ingredient_id) REFERENCES ingredients(id)
);


CREATE TABLE product_extras (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_id INTEGER NOT NULL,
    extra_id INTEGER NOT NULL,
    amount INTEGER NOT NULL,

    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (extra_id) REFERENCES extras(id)
);

