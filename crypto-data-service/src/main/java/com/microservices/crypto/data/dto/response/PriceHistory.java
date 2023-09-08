package com.microservices.crypto.data.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PriceHistory {
    private String change;
    private JsonNode history;
}
