package com.microservices.crypto.data.service;

import com.microservices.crypto.data.dto.response.CoinResponse;
import com.microservices.crypto.data.dto.response.CoinDashboardResponse;
import com.microservices.crypto.data.dto.response.GlobalStatisticsResponse;
import com.microservices.crypto.data.dto.response.PriceHistory;

import java.io.IOException;


public interface CoinsDataService {
    CoinDashboardResponse getData(String offset, String limit,
                                  String search, String orderBy,
                                  String orderDirection, String timePeriod
    ) throws IOException;

    CoinResponse getCoin(String id, String timePeriod) throws IOException;
    GlobalStatisticsResponse getStats() throws IOException;
    PriceHistory getCoinHistory(String id, String timePeriod) throws IOException;
}
