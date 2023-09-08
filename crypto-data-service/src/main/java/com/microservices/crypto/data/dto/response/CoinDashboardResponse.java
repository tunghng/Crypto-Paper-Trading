package com.microservices.crypto.data.dto.response;

import com.microservices.crypto.data.dto.dto.CoinDto;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;

@Builder
@Data
public class CoinDashboardResponse {
    private Collection<CoinDto> coins;

}
