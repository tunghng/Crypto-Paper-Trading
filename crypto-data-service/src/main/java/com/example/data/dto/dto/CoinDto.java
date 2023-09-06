package com.example.data.dto.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CoinDto {
    private String uuid;
    private String symbol;
    private String name;
    private String price;
    private String iconUrl;
    private String marketCap;
    private String change;
    private String volume;
    private Collection<String> sparkline;
}
