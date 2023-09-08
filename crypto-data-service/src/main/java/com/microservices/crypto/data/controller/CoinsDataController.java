package com.microservices.crypto.data.controller;

import com.microservices.crypto.data.service.CoinsDataService;
import com.microservices.crypto.data.dto.response.CoinResponse;
import com.microservices.crypto.data.dto.response.CoinDashboardResponse;
import com.microservices.crypto.data.dto.response.GlobalStatisticsResponse;
import com.microservices.crypto.data.dto.response.PriceHistory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("data")
public class CoinsDataController {
    private final CoinsDataService dataService;

    @Autowired
    public CoinsDataController(CoinsDataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("")
    @Operation(summary = "Get Coins")
    public ResponseEntity<CoinDashboardResponse> getData(
            @Parameter(description = "Sequence number of page starting from 0")
            @RequestParam(defaultValue = "0") String offset,
            @Parameter(description = "Maximum amount of entities in a one page")
            @RequestParam(defaultValue = "10") String limit,
            @Parameter(description = "Search text")
            @RequestParam(required = false) String search,
            @Parameter(description = "Order by (price marketCap 24hVolume change listedAt)")
            @RequestParam(defaultValue = "marketCap") String orderBy,
            @Parameter(description = "Order direction (desc asc)")
            @RequestParam(defaultValue = "desc") String orderDirection,
            @Parameter(description = "Time period (3h 24h 7d 30d 3m 1y 3y 5y)")
            @RequestParam(defaultValue = "24h") String timePeriod
    ) throws IOException {
        return ResponseEntity.ok(dataService.getData(offset, limit, search, orderBy, orderDirection, timePeriod));
    }

    @GetMapping("statistics")
    @Operation(summary = "Get global statistics")
    public ResponseEntity<GlobalStatisticsResponse> getStats() throws IOException {
        return ResponseEntity.ok(dataService.getStats());
    }

    @GetMapping("coin")
    @Operation(summary = "Get coin")
    public ResponseEntity<CoinResponse> getCoin(
            @Parameter(description = "Id of coin")
            @RequestParam String id,
            @Parameter(description = "Time period (3h 24h 7d 30d 3m 1y 3y 5y)")
            @RequestParam(defaultValue = "24h") String timePeriod
    ) throws IOException {
        return ResponseEntity.ok(dataService.getCoin(id, timePeriod));
    }

    @GetMapping("coin/history")
    @Operation(summary = "Get coin price history")
    public ResponseEntity<PriceHistory> getHistory(
            @Parameter(description = "Id of coin")
            @RequestParam String id,
            @Parameter(description = "Time period (3h 24h 7d 30d 3m 1y 3y 5y)")
            @RequestParam(defaultValue = "24h") String timePeriod
    ) throws IOException {
        return ResponseEntity.ok(dataService.getCoinHistory(id, timePeriod));
    }
}
