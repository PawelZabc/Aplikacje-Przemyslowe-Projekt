package com.projekt.kiosk.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalesStatsDto {

    private LocalDate date;
    private Integer orderCount;
    private Long totalRevenueCents;

    public String getFormattedRevenue() {
        if (totalRevenueCents == null) {
            return "0.00";
        }
        return String.format(java.util.Locale.US, "%.2f", totalRevenueCents / 100.0);
    }
}
