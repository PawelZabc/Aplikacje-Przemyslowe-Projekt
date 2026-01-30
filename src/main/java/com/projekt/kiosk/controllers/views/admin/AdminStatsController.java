package com.projekt.kiosk.controllers.views.admin;

import com.projekt.kiosk.dao.SalesStatsDao;
import com.projekt.kiosk.dto.stats.SalesStatsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/stats")
@Slf4j
public class AdminStatsController {

    private final SalesStatsDao salesStatsDao;

    public AdminStatsController(SalesStatsDao salesStatsDao) {
        this.salesStatsDao = salesStatsDao;
    }

    @GetMapping
    public String statsPage(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Model model) {

        if (from == null) {
            from = LocalDate.now().minusDays(30);
        }
        if (to == null) {
            to = LocalDate.now();
        }

        log.info("Fetching sales stats from {} to {}", from, to);

        List<SalesStatsDto> dailyStats = salesStatsDao.getDailyStatsInRange(from, to);
        SalesStatsDto totalStats = salesStatsDao.getTotalStats();

        int periodOrderCount = dailyStats.stream()
                .mapToInt(SalesStatsDto::getOrderCount)
                .sum();
        long periodRevenueCents = dailyStats.stream()
                .mapToLong(SalesStatsDto::getTotalRevenueCents)
                .sum();

        model.addAttribute("dailyStats", dailyStats);
        model.addAttribute("totalStats", totalStats);
        model.addAttribute("periodOrderCount", periodOrderCount);
        model.addAttribute("periodRevenue", String.format("%.2f", periodRevenueCents / 100.0));
        model.addAttribute("fromDate", from);
        model.addAttribute("toDate", to);

        return "admin/stats";
    }
}
