package org.example.final_btl_datve.service;

import org.example.final_btl_datve.dto.MovieViewData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StatisticsService {
    Long getTotalTicketSold();
    Double getTotalRevenue();
    List<MovieViewData> getTopMovie();

    Long getTotalUser();
    Long getTotalMovie();
}
