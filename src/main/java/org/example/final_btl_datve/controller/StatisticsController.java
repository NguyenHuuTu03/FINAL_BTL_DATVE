package org.example.final_btl_datve.controller;

import org.example.final_btl_datve.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/statistics")
public class StatisticsController {
    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/total-ticket-sold")
    public ResponseEntity<Long> getTotalTicketSold() {
        return ResponseEntity.ok(statisticsService.getTotalTicketSold());
    }

    @GetMapping("/total-revenue")
    public ResponseEntity<Double> getTotalRevenue() {
        return ResponseEntity.ok(statisticsService.getTotalRevenue());
    }

    @GetMapping("/total-user")
    public ResponseEntity<Long> getTotalUser() {
        return ResponseEntity.ok(statisticsService.getTotalUser());
    }

    @GetMapping("/total-movie")
    public ResponseEntity<Long> getTotalMovie() {
        return ResponseEntity.ok(statisticsService.getTotalMovie());
    }

    @GetMapping("/top-movie")
    public ResponseEntity<?> getTopMovie() {
        return ResponseEntity.ok(statisticsService.getTopMovie());
    }

}
