package com.microservices.crypto.data.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CoinResponse {
    private String symbol;
    private String name;
    private String price;
    private String iconUrl;
    private String marketCap;
    private String change;
    private String volume;
    private JsonNode sparkline;
    private String description;
    private String websiteUrl;
    private JsonNode links;
    private JsonNode supply;
    private JsonNode allTimeHigh;
}
