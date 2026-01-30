package com.projekt.kiosk.dao;

import com.projekt.kiosk.entities.ExtraEntity;
import com.projekt.kiosk.entities.IngredientEntity;
import com.projekt.kiosk.entities.ProductEntity;
import com.projekt.kiosk.entities.order.OrderEntity;
import com.projekt.kiosk.dto.ExtraDto;
import com.projekt.kiosk.dto.IngredientDto;
import com.projekt.kiosk.dto.cart.Cart;
import com.projekt.kiosk.dto.cart.CartItemDto;
import com.projekt.kiosk.enums.OrderType;
import com.projekt.kiosk.mappers.rowmappers.ExtraRowMapper;
import com.projekt.kiosk.mappers.rowmappers.IngredientRowMapper;
import com.projekt.kiosk.mappers.rowmappers.ProductRowMapper;
import com.projekt.kiosk.repositories.order.OrderNumberGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Slf4j
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
                log.info("Starting order snapshot creation. Items count: {}, Order type: {}",
                                cart.getItems().size(), cart.getOrderType());

                String orderNumber = orderNumberGenerator.generateOrderNumber();
                Timestamp now = Timestamp.valueOf(LocalDateTime.now());
                OrderType orderType = cart.getOrderType() != null ? cart.getOrderType() : OrderType.DINE_IN;
                int packagingFeeCents = orderType.getPackagingFeeCents();

                List<Long> orderIds = jdbcTemplate.query(
                                "INSERT INTO orders(order_number, created_at, total_price_cents, order_type, packaging_fee_cents) "
                                                +
                                                "VALUES (?, ?, ?, ?, ?) RETURNING id",
                                (rs, rowNum) -> rs.getLong("id"),
                                orderNumber,
                                now,
                                0,
                                orderType.name(),
                                packagingFeeCents);

                Long orderId = orderIds.getFirst();
                int itemsTotalCents = 0;

                for (CartItemDto itemDto : cart.getItems()) {

                        List<ProductEntity> products = jdbcTemplate.query(
                                        "SELECT id, name, price_cents FROM products WHERE id = ?",
                                        new ProductRowMapper(),
                                        itemDto.getProductId());

                        if (products.isEmpty()) {
                                log.error("Invalid productId: {} - product not found", itemDto.getProductId());
                                throw new IllegalArgumentException("Invalid productId: " + itemDto.getProductId());
                        }

                        ProductEntity product = products.getFirst();
                        int basePriceCents = product.getPriceCents();

                        if (basePriceCents != itemDto.getBasePriceCents()) {
                                log.error("Price mismatch for productId {}. Expected: {}, Got: {}",
                                                product.getId(), basePriceCents, itemDto.getBasePriceCents());
                                throw new IllegalArgumentException(
                                                "Invalid basePriceCents for productId " + product.getId());
                        }

                        int quantity = itemDto.getQuantity();

                        List<Long> orderItemIds = jdbcTemplate.query(
                                        "INSERT INTO order_items(order_id, product_name, base_price_cents, quantity) " +
                                                        "VALUES (?, ?, ?, ?) RETURNING id",
                                        (rs, rowNum) -> rs.getLong("id"),
                                        orderId,
                                        product.getName(),
                                        basePriceCents,
                                        quantity);

                        Long orderItemId = orderItemIds.getFirst();

                        // Process removed ingredients
                        for (IngredientDto ing : itemDto.getRemovedIngredients()) {

                                List<IngredientEntity> ingredients = jdbcTemplate.query(
                                                "SELECT i.id, i.name FROM ingredients i " +
                                                                "JOIN product_ingredients pi ON pi.ingredient_id = i.id "
                                                                +
                                                                "WHERE pi.product_id = ? AND i.id = ?",
                                                new IngredientRowMapper(),
                                                product.getId(),
                                                ing.getId());

                                if (ingredients.isEmpty()) {
                                        log.error("Invalid ingredientId {} for productId {}", ing.getId(),
                                                        product.getId());
                                        throw new IllegalArgumentException(
                                                        "Invalid ingredientId " + ing.getId() +
                                                                        " for productId " + product.getId());
                                }

                                jdbcTemplate.update(
                                                "INSERT INTO order_item_ingredients(order_item_id, ingredient_name) " +
                                                                "VALUES (?, ?)",
                                                orderItemId,
                                                ing.getName());
                        }

                        // Process extras
                        int extrasTotalCents = 0;

                        for (ExtraDto extra : itemDto.getAddedExtras()) {

                                List<ExtraEntity> extras = jdbcTemplate.query(
                                                "SELECT e.id, e.name, e.price_cents FROM extras e " +
                                                                "JOIN product_extras pe ON pe.extra_id = e.id " +
                                                                "WHERE pe.product_id = ? AND e.id = ?",
                                                new ExtraRowMapper(),
                                                product.getId(),
                                                extra.getId());

                                if (extras.isEmpty()) {
                                        log.error("Invalid extraId {} for productId {}", extra.getId(),
                                                        product.getId());
                                        throw new IllegalArgumentException(
                                                        "Invalid extraId " + extra.getId() +
                                                                        " for productId " + product.getId());
                                }

                                int extraPriceCents = extras.getFirst().getPriceCents();
                                extrasTotalCents += extraPriceCents;

                                jdbcTemplate.update(
                                                "INSERT INTO order_item_extras(order_item_id, extra_name, price_cents) "
                                                                +
                                                                "VALUES (?, ?, ?)",
                                                orderItemId,
                                                extra.getName(),
                                                extraPriceCents);
                        }

                        itemsTotalCents += (basePriceCents * quantity) +
                                        (extrasTotalCents * quantity);
                }

                // Total includes items + packaging fee
                int totalPriceCents = itemsTotalCents + packagingFeeCents;

                jdbcTemplate.update(
                                "UPDATE orders SET total_price_cents = ? WHERE id = ?",
                                totalPriceCents,
                                orderId);

                OrderEntity order = new OrderEntity();
                order.setId(orderId);
                order.setOrderNumber(orderNumber);
                order.setCreatedAt(now.toLocalDateTime());
                order.setTotalPriceCents(totalPriceCents);
                order.setOrderType(orderType);
                order.setPackagingFeeCents(packagingFeeCents);

                log.info("Order created successfully. Order number: {}, Total: {} cents, Type: {}",
                                orderNumber, totalPriceCents, orderType);

                return order;
        }
}
