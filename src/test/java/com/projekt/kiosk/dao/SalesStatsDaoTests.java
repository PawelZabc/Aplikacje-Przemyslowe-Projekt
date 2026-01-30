package com.projekt.kiosk.dao;

import com.projekt.kiosk.dto.stats.SalesStatsDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for SalesStatsDao with GROUP BY queries.
 */
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SalesStatsDaoTests {

        @Autowired
        private SalesStatsDao salesStatsDao;

        @Autowired
        private JdbcTemplate jdbcTemplate;

        @Test
        public void testGetDailyStatsNoOrders() {
                SalesStatsDto stats = salesStatsDao.getDailyStats(LocalDate.now());

                assertThat(stats).isNotNull();
                assertThat(stats.getDate()).isEqualTo(LocalDate.now());
                assertThat(stats.getOrderCount()).isEqualTo(0);
                assertThat(stats.getTotalRevenueCents()).isEqualTo(0L);
        }

        @Test
        public void testGetDailyStatsWithOrders() {
                // Insert test order
                jdbcTemplate.update(
                                "INSERT INTO orders (id, order_number, created_at, total_price_cents, order_type, packaging_fee_cents) "
                                                +
                                                "VALUES (nextval('order_seq'), ?, NOW(), ?, 'DINE_IN', 0)",
                                "TEST001", 1500);
                jdbcTemplate.update(
                                "INSERT INTO orders (id, order_number, created_at, total_price_cents, order_type, packaging_fee_cents) "
                                                +
                                                "VALUES (nextval('order_seq'), ?, NOW(), ?, 'TAKEAWAY', 100)",
                                "TEST002", 2500);

                SalesStatsDto stats = salesStatsDao.getDailyStats(LocalDate.now());

                assertThat(stats).isNotNull();
                assertThat(stats.getOrderCount()).isEqualTo(2);
                assertThat(stats.getTotalRevenueCents()).isEqualTo(4000L);
        }

        @Test
        public void testGetDailyStatsInRange() {
                jdbcTemplate.update(
                                "INSERT INTO orders (id, order_number, created_at, total_price_cents, order_type, packaging_fee_cents) "
                                                +
                                                "VALUES (nextval('order_seq'), ?, NOW(), ?, 'DINE_IN', 0)",
                                "TEST001", 1000);

                LocalDate from = LocalDate.now().minusDays(7);
                LocalDate to = LocalDate.now();

                List<SalesStatsDto> stats = salesStatsDao.getDailyStatsInRange(from, to);

                assertThat(stats).isNotNull();
                assertThat(stats).hasSizeGreaterThanOrEqualTo(1);
        }

        @Test
        public void testGetTotalStats() {
                jdbcTemplate.update(
                                "INSERT INTO orders (id, order_number, created_at, total_price_cents, order_type, packaging_fee_cents) "
                                                +
                                                "VALUES (nextval('order_seq'), ?, NOW(), ?, 'DINE_IN', 0)",
                                "TOTAL001", 5000);

                SalesStatsDto stats = salesStatsDao.getTotalStats();

                assertThat(stats).isNotNull();
                assertThat(stats.getOrderCount()).isGreaterThanOrEqualTo(1);
                assertThat(stats.getTotalRevenueCents()).isGreaterThanOrEqualTo(5000L);
        }

        @Test
        public void testFormattedRevenue() {
                SalesStatsDto stats = SalesStatsDto.builder()
                                .date(LocalDate.now())
                                .orderCount(5)
                                .totalRevenueCents(12350L)
                                .build();

                assertThat(stats.getFormattedRevenue()).isEqualTo("123.50");
        }
}
