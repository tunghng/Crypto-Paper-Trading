package com.microservices.crypto.data.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GlobalStatisticsResponse {
    private String totalCoins;
    private String totalMarkets;
    private String totalMarketCap;
    private String total24hVolume;
    private String btcDominance;
    private JsonNode bestCoins;
    private JsonNode newestCoins;

}
