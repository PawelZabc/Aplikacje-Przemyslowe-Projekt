package com.projekt.kiosk.repositories.order;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Repository
public class OrderNumberGenerator {

    private final JdbcTemplate jdbcTemplate;

    public OrderNumberGenerator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public String generateOrderNumber() {
        LocalDate today = LocalDate.now();

        // Try to select the counter with a lock
        Integer lastNumber = jdbcTemplate.query(
                "SELECT last_number FROM daily_order_counter WHERE order_date = ? FOR UPDATE",
                new Object[]{today},
                rs -> rs.next() ? rs.getInt("last_number") : null
        );

        int nextNumber;
        if (lastNumber == null) {
            nextNumber = 0;
            jdbcTemplate.update(
                    "INSERT INTO daily_order_counter(order_date, last_number) VALUES (?, ?)",
                    today, nextNumber
            );
        } else {
            nextNumber = lastNumber + 1;
            if (nextNumber > 999) nextNumber = 0;

            jdbcTemplate.update(
                    "UPDATE daily_order_counter SET last_number = ? WHERE order_date = ?",
                    nextNumber, today
            );
        }

        return String.format("%03d", nextNumber);
    }
}
