package com.projekt.kiosk.dao;

import com.projekt.kiosk.entities.ExtraEntity;
import com.projekt.kiosk.entities.IngredientEntity;
import com.projekt.kiosk.entities.ProductEntity;
import com.projekt.kiosk.entities.order.OrderEntity;
import com.projekt.kiosk.dto.ExtraDto;
import com.projekt.kiosk.dto.IngredientDto;
import com.projekt.kiosk.dto.cart.Cart;
import com.projekt.kiosk.dto.cart.CartItemDto;
import com.projekt.kiosk.mappers.rowmappers.ExtraRowMapper;
import com.projekt.kiosk.mappers.rowmappers.IngredientRowMapper;
import com.projekt.kiosk.mappers.rowmappers.ProductRowMapper;
import com.projekt.kiosk.repositories.order.OrderNumberGenerator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class OrderDaoImpl implements OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final OrderNumberGenerator orderNumberGenerator;

    public OrderDaoImpl(JdbcTemplate jdbcTemplate, OrderNumberGenerator orderNumberGenerator) {
        this.jdbcTemplate = jdbcTemplate;
        this.orderNumberGenerator = orderNumberGenerator;
    }

    @Override
    @Transactional
    public OrderEntity saveOrderSnapshot(Cart cart) {

        String orderNumber = orderNumberGenerator.generateOrderNumber();
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());

        List<Long> orderIds = jdbcTemplate.query(
                "INSERT INTO orders(order_number, created_at, total_price_cents) " +
                        "VALUES (?, ?, ?) RETURNING id",
                (rs, rowNum) -> rs.getLong("id"),
                orderNumber,
                now,
                0
        );

        Long orderId = orderIds.getFirst();
        int totalPriceCents = 0;

        for (CartItemDto itemDto : cart.getItems()) {

            List<ProductEntity> products = jdbcTemplate.query(
                    "SELECT id, name, price_cents FROM products WHERE id = ?",
                    new ProductRowMapper(),
                    itemDto.getProductId()
            );

            if (products.isEmpty()) {
                throw new IllegalArgumentException("Invalid productId: " + itemDto.getProductId());
            }

            ProductEntity product = products.getFirst();
            int basePriceCents = product.getPriceCents();

            if (basePriceCents != itemDto.getBasePriceCents()) {
                throw new IllegalArgumentException(
                        "Invalid basePriceCents for productId " + product.getId()
                );
            }

            int quantity = itemDto.getQuantity();

            List<Long> orderItemIds = jdbcTemplate.query(
                    "INSERT INTO order_items(order_id, product_name, base_price_cents, quantity) " +
                            "VALUES (?, ?, ?, ?) RETURNING id",
                    (rs, rowNum) -> rs.getLong("id"),
                    orderId,
                    product.getName(),
                    basePriceCents,
                    quantity
            );

            Long orderItemId = orderItemIds.getFirst();


            for (IngredientDto ing : itemDto.getRemovedIngredients()) {

                List<IngredientEntity> ingredients = jdbcTemplate.query(
                        "SELECT i.id, i.name FROM ingredients i " +
                                "JOIN product_ingredients pi ON pi.ingredient_id = i.id " +
                                "WHERE pi.product_id = ? AND i.id = ?",
                        new IngredientRowMapper(),
                        product.getId(),
                        ing.getId()
                );

                if (ingredients.isEmpty()) {
                    throw new IllegalArgumentException(
                            "Invalid ingredientId " + ing.getId() +
                                    " for productId " + product.getId()
                    );
                }

                jdbcTemplate.update(
                        "INSERT INTO order_item_ingredients(order_item_id, ingredient_name) " +
                                "VALUES (?, ?)",
                        orderItemId,
                        ing.getName()
                );
            }

            // 6️⃣ Validate & insert extras
            int extrasTotalCents = 0;

            for (ExtraDto extra : itemDto.getAddedExtras()) {

                List<ExtraEntity> extras = jdbcTemplate.query(
                        "SELECT e.id, e.name, e.price_cents FROM extras e " +
                                "JOIN product_extras pe ON pe.extra_id = e.id " +
                                "WHERE pe.product_id = ? AND e.id = ?",
                        new ExtraRowMapper(),
                        product.getId(),
                        extra.getId()
                );

                if (extras.isEmpty()) {
                    throw new IllegalArgumentException(
                            "Invalid extraId " + extra.getId() +
                                    " for productId " + product.getId()
                    );
                }

                int extraPriceCents = extras.getFirst().getPriceCents();
                extrasTotalCents += extraPriceCents;

                jdbcTemplate.update(
                        "INSERT INTO order_item_extras(order_item_id, extra_name, price_cents) " +
                                "VALUES (?, ?, ?)",
                        orderItemId,
                        extra.getName(),
                        extraPriceCents
                );
            }

            totalPriceCents +=
                    (basePriceCents * quantity) +
                            (extrasTotalCents * quantity);
        }

        jdbcTemplate.update(
                "UPDATE orders SET total_price_cents = ? WHERE id = ?",
                totalPriceCents,
                orderId
        );

        OrderEntity order = new OrderEntity();
        order.setId(orderId);
        order.setOrderNumber(orderNumber);
        order.setCreatedAt(now.toLocalDateTime());
        order.setTotalPriceCents(totalPriceCents);


        return order;
    }
}
