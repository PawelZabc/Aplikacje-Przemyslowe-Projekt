DROP TABLE IF EXISTS order_item_extras;
DROP TABLE IF EXISTS order_item_ingredients;
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;

DROP TABLE IF EXISTS product_ingredients;
DROP TABLE IF EXISTS product_extras;
DROP TABLE IF EXISTS extras;
DROP TABLE IF EXISTS ingredients;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS daily_order_counter;
DROP TABLE IF EXISTS categories;

CREATE TABLE categories (
                             id SERIAL PRIMARY KEY,
                             name VARCHAR(50) NOT NULL
);

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
                          price_cents INTEGER NOT NULL CHECK (price_cents >= 0),
                          category_id INTEGER REFERENCES categories(id)
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
                        order_number VARCHAR(20) NOT NULL,
                        created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                        total_price_cents INTEGER NOT NULL CHECK (total_price_cents >= 0),
                        order_type VARCHAR(20) NOT NULL DEFAULT 'DINE_IN',
                        packaging_fee_cents INTEGER NOT NULL DEFAULT 0 CHECK (packaging_fee_cents >= 0)
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

CREATE TABLE daily_order_counter (
                                     order_date DATE PRIMARY KEY,
                                     last_number INTEGER NOT NULL
);
