-- =========================
-- INGREDIENTS
-- =========================
INSERT INTO ingredients (name) VALUES
                                   ('Bun'),
                                   ('Beef Patty'),
                                   ('Cheese'),
                                   ('Lettuce'),
                                   ('Tomato'),
                                   ('Onion'),
                                   ('Pickles'),
                                   ('Sauce');

-- =========================
-- EXTRAS
-- =========================
INSERT INTO extras (name, price_cents) VALUES
                                           ('Extra Cheese', 200),
                                           ('Bacon', 300),
                                           ('Jalapenos', 150),
                                           ('Fried Egg', 250),
                                           ('Extra Sauce', 100);

-- =========================
-- PRODUCTS
-- =========================
INSERT INTO products (name, price_cents) VALUES
                                             ('Classic Burger', 1500),
                                             ('Cheeseburger', 1600),
                                             ('Bacon Burger', 1800),
                                             ('Veggie Burger', 1400);

-- =========================
-- PRODUCT_INGREDIENTS
-- =========================

-- Classic Burger (product_id = 1)
INSERT INTO product_ingredients (product_id, ingredient_id) VALUES
                                                                (1, 1),
                                                                (1, 2),
                                                                (1, 4),
                                                                (1, 5),
                                                                (1, 8);

-- Cheeseburger (product_id = 2)
INSERT INTO product_ingredients (product_id, ingredient_id) VALUES
                                                                (2, 1),
                                                                (2, 2),
                                                                (2, 3),
                                                                (2, 4),
                                                                (2, 8);

-- Bacon Burger (product_id = 3)
INSERT INTO product_ingredients (product_id, ingredient_id) VALUES
                                                                (3, 1),
                                                                (3, 2),
                                                                (3, 3),
                                                                (3, 6),
                                                                (3, 8);

-- Veggie Burger (product_id = 4)
INSERT INTO product_ingredients (product_id, ingredient_id) VALUES
                                                                (4, 1),
                                                                (4, 4),
                                                                (4, 5),
                                                                (4, 7),
                                                                (4, 8);

-- =========================
-- PRODUCT_EXTRAS
-- =========================

-- Classic Burger
INSERT INTO product_extras (product_id, extra_id) VALUES
                                                      (1, 1),
                                                      (1, 3),
                                                      (1, 5);

-- Cheeseburger
INSERT INTO product_extras (product_id, extra_id) VALUES
                                                      (2, 1),
                                                      (2, 2),
                                                      (2, 5);

-- Bacon Burger
INSERT INTO product_extras (product_id, extra_id) VALUES
                                                      (3, 1),
                                                      (3, 2),
                                                      (3, 4);

-- Veggie Burger
INSERT INTO product_extras (product_id, extra_id) VALUES
                                                      (4, 1),
                                                      (4, 3),
                                                      (4, 5);
