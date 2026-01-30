package com.projekt.kiosk.dao;

import com.projekt.kiosk.dto.stats.SalesStatsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * Implementation of SalesStatsDao using JdbcTemplate with GROUP BY queries.
 */
@Repository
@Slf4j
public class SalesStatsDaoImpl implements SalesStatsDao {

        private final JdbcTemplate jdbcTemplate;

        public SalesStatsDaoImpl(JdbcTemplate jdbcTemplate) {
                this.jdbcTemplate = jdbcTemplate;
        }

        @Override
        @Transactional(readOnly = true)
        public SalesStatsDto getDailyStats(LocalDate date) {
                log.debug("Fetching daily stats for date: {}", date);

                List<SalesStatsDto> results = jdbcTemplate.query(
                                """
                                                SELECT CAST(created_at AS DATE) as date,
                                                       COUNT(*) as order_count,
                                                       COALESCE(SUM(total_price_cents), 0) as total_revenue_cents
                                                FROM orders
                                                WHERE CAST(created_at AS DATE) = ?
                                                GROUP BY CAST(created_at AS DATE)
                                                """,
                                (rs, rowNum) -> SalesStatsDto.builder()
                                                .date(rs.getDate("date").toLocalDate())
                                                .orderCount(rs.getInt("order_count"))
                                                .totalRevenueCents(rs.getLong("total_revenue_cents"))
                                                .build(),
                                date);

                if (results.isEmpty()) {
                        log.debug("No orders found for date: {}", date);
                        return SalesStatsDto.builder()
                                        .date(date)
                                        .orderCount(0)
                                        .totalRevenueCents(0L)
                                        .build();
                }

                return results.getFirst();
        }

        @Override
        @Transactional(readOnly = true)
        public List<SalesStatsDto> getDailyStatsInRange(LocalDate from, LocalDate to) {
                log.info("Fetching daily stats from {} to {}", from, to);

                return jdbcTemplate.query(
                                """
                                                SELECT CAST(created_at AS DATE) as date,
                                                       COUNT(*) as order_count,
                                                       COALESCE(SUM(total_price_cents), 0) as total_revenue_cents
                                                FROM orders
                                                WHERE CAST(created_at AS DATE) >= ? AND CAST(created_at AS DATE) <= ?
                                                GROUP BY CAST(created_at AS DATE)
                                                ORDER BY date ASC
                                                """,
                                (rs, rowNum) -> SalesStatsDto.builder()
                                                .date(rs.getDate("date").toLocalDate())
                                                .orderCount(rs.getInt("order_count"))
                                                .totalRevenueCents(rs.getLong("total_revenue_cents"))
                                                .build(),
                                from, to);
        }

        @Override
        @Transactional(readOnly = true)
        public List<SalesStatsDto> getMonthlyStats(YearMonth month) {
                log.info("Fetching monthly stats for: {}", month);

                LocalDate from = month.atDay(1);
                LocalDate to = month.atEndOfMonth();

                return getDailyStatsInRange(from, to);
        }

        @Override
        @Transactional(readOnly = true)
        public SalesStatsDto getTotalStats() {
                log.debug("Fetching total (all-time) stats");

                List<SalesStatsDto> results = jdbcTemplate.query(
                                """
                                                SELECT COUNT(*) as order_count,
                                                       COALESCE(SUM(total_price_cents), 0) as total_revenue_cents
                                                FROM orders
                                                """,
                                (rs, rowNum) -> SalesStatsDto.builder()
                                                .date(null)
                                                .orderCount(rs.getInt("order_count"))
                                                .totalRevenueCents(rs.getLong("total_revenue_cents"))
                                                .build());

                if (results.isEmpty()) {
                        return SalesStatsDto.builder()
                                        .date(null)
                                        .orderCount(0)
                                        .totalRevenueCents(0L)
                                        .build();
                }

                return results.getFirst();
        }
}
