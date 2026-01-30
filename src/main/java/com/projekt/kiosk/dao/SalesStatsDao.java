package com.projekt.kiosk.dao;

import com.projekt.kiosk.dto.stats.SalesStatsDto;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * Data Access Object for sales statistics.
 */
public interface SalesStatsDao {

    /**
     * Get daily statistics for a specific date.
     */
    SalesStatsDto getDailyStats(LocalDate date);

    /**
     * Get daily statistics for a date range.
     */
    List<SalesStatsDto> getDailyStatsInRange(LocalDate from, LocalDate to);

    /**
     * Get aggregated monthly statistics.
     */
    List<SalesStatsDto> getMonthlyStats(YearMonth month);

    /**
     * Get total statistics (all time).
     */
    SalesStatsDto getTotalStats();
}
