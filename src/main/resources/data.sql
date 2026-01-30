-- =========================
-- CATEGORIES
-- =========================
INSERT INTO categories (name) VALUES
                                  ('Burgers'),
                                  ('Vegetarian'),
                                  ('Sides'),
                                  ('Drinks');

-- =========================
-- INGREDIENTS
-- =========================
INSERT INTO ingredients (name) VALUES
                                   ('Bread'),
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
INSERT INTO products (name, price_cents, category_id) VALUES
                                                          ('Classic Burger', 1500, 1),
                                                          ('Cheeseburger', 1600, 1),
                                                          ('Bacon Burger', 1800, 1),
                                                          ('Veggie Burger', 1400, 2);

-- =========================
-- PRODUCT_INGREDIENTS
-- =========================

INSERT INTO product_ingredients (product_id, ingredient_id) VALUES
                                                                (1, 1),
                                                                (1, 2),
                                                                (1, 4),
                                                                (1, 5),
                                                                (1, 8);

INSERT INTO product_ingredients (product_id, ingredient_id) VALUES
                                                                (2, 1),
                                                                (2, 2),
                                                                (2, 3),
                                                                (2, 4),
                                                                (2, 8);

INSERT INTO product_ingredients (product_id, ingredient_id) VALUES
                                                                (3, 1),
                                                                (3, 2),
                                                                (3, 3),
                                                                (3, 6),
                                                                (3, 8);

INSERT INTO product_ingredients (product_id, ingredient_id) VALUES
                                                                (4, 1),
                                                                (4, 4),
                                                                (4, 5),
                                                                (4, 7),
                                                                (4, 8);

-- =========================
-- PRODUCT_EXTRAS
-- =========================

INSERT INTO product_extras (product_id, extra_id) VALUES
                                                      (1, 1),
                                                      (1, 3),
                                                      (1, 5);

INSERT INTO product_extras (product_id, extra_id) VALUES
                                                      (2, 1),
                                                      (2, 2),
                                                      (2, 5);

INSERT INTO product_extras (product_id, extra_id) VALUES
                                                      (3, 1),
                                                      (3, 2),
                                                      (3, 4);

INSERT INTO product_extras (product_id, extra_id) VALUES
                                                      (4, 1),
                                                      (4, 3),
                                                      (4, 5);
