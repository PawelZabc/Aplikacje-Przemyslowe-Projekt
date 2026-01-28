-- INGREDIENTS
INSERT INTO ingredients (id, name) VALUES
                                       (1, 'Bun'),
                                       (2, 'Beef Patty'),
                                       (3, 'Cheese'),
                                       (4, 'Lettuce'),
                                       (5, 'Tomato'),
                                       (6, 'Onion'),
                                       (7, 'Pickles'),
                                       (8, 'Sauce');

-- EXTRAS
INSERT INTO extras (id, name, price_cents) VALUES
                                               (1, 'Extra Cheese', 200),
                                               (2, 'Bacon', 300),
                                               (3, 'Jalapenos', 150),
                                               (4, 'Fried Egg', 250),
                                               (5, 'Extra Sauce', 100);

-- PRODUCTS
INSERT INTO products (id, name, price_cents) VALUES
                                                 (1, 'Classic Burger', 1500),
                                                 (2, 'Cheeseburger', 1600),
                                                 (3, 'Bacon Burger', 1800),
                                                 (4, 'Veggie Burger', 1400);

-- PRODUCT_INGREDIENTS
-- Classic Burger
INSERT INTO product_ingredients (id, product_id, ingredient_id) VALUES
                                                                    (1, 1, 1),
                                                                    (2, 1, 2), -- Beef
                                                                    (3, 1, 4), -- Lettuce
                                                                    (4, 1, 5), -- Tomato
                                                                    (5, 1, 8); -- Sauce

-- Cheeseburger
INSERT INTO product_ingredients (id, product_id, ingredient_id) VALUES
                                                                    (6, 2, 1),
                                                                    (7, 2, 2),
                                                                    (8, 2, 3),
                                                                    (9, 2, 4),
                                                                    (10, 2, 8);

-- Bacon Burger
INSERT INTO product_ingredients (id, product_id, ingredient_id) VALUES
                                                                    (11, 3, 1),
                                                                    (12, 3, 2),
                                                                    (13, 3, 3),
                                                                    (14, 3, 6),
                                                                    (15, 3, 8);

-- Veggie Burger
INSERT INTO product_ingredients (id, product_id, ingredient_id) VALUES
                                                                    (16, 4, 1),
                                                                    (17, 4, 4),
                                                                    (18, 4, 5),
                                                                    (19, 4, 7),
                                                                    (20, 4, 8);

-- PRODUCT_EXTRAS (allowed extras per product)
INSERT INTO product_extras (id, product_id, extra_id) VALUES
                                                          (1, 1, 1),
                                                          (2, 1, 3),
                                                          (3, 1, 5),

                                                          (4, 2, 1),
                                                          (5, 2, 2),
                                                          (6, 2, 5),

                                                          (7, 3, 1),
                                                          (8, 3, 2),
                                                          (9, 3, 4),

                                                          (10, 4, 1),
                                                          (11, 4, 3),
                                                          (12, 4, 5);

-- -- SAMPLE ORDERS (business analytics demo)
-- INSERT INTO orders (id, order_number, created_at, total_price_cents) VALUES
--                                                                          (1, '#001', CURRENT_TIMESTAMP, 1800),
--                                                                          (2, '#002', CURRENT_TIMESTAMP, 2100);
--
-- -- ORDER ITEMS
-- INSERT INTO order_items (id, order_id, product_name, base_price_cents, quantity) VALUES
--                                                                                      (1, 1, 'Classic Burger', 1500, 1),
--                                                                                      (2, 2, 'Bacon Burger', 1800, 1);
--
-- -- ORDER ITEM INGREDIENTS
-- INSERT INTO order_item_ingredients (id, order_item_id, ingredient_name) VALUES
--                                                                             (1, 1, 'Bun'),
--                                                                             (2, 1, 'Beef Patty'),
--                                                                             (3, 1, 'Lettuce'),
--
--                                                                             (4, 2, 'Bun'),
--                                                                             (5, 2, 'Beef Patty'),
--                                                                             (6, 2, 'Cheese');
--
-- -- ORDER ITEM EXTRAS
-- INSERT INTO order_item_extras (id, order_item_id, extra_name, price_cents) VALUES
--                                                                                (1, 1, 'Extra Cheese', 200),
--                                                                                (2, 2, 'Bacon', 300);