package com.example.data.service;

import com.example.data.dto.dto.CoinDto;
import com.example.data.dto.response.CoinResponse;
import com.example.data.dto.response.CoinDashboardResponse;
import com.example.data.dto.response.GlobalStatisticsResponse;
import com.example.data.dto.response.PriceHistory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@XmlRootElement
@Service
public class CoinsDataServiceImpl implements CoinsDataService {

    @Value("${api.key}")
    private String apiKey;
    @Value("${api.host}")
    private String apiHost;
    @Value("${api.url}")
    private String apiUrl;

    OkHttpClient client = new OkHttpClient();
    ObjectMapper mapper = new ObjectMapper();

    @Override
    public CoinDashboardResponse getData(String offset, String limit,
                                         String search, String orderBy,
                                         String orderDirection, String timePeriod)
            throws IOException {
        Collection<CoinDto> coinData = new ArrayList<>();

        String[] pathSegments = {"coins"};
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("referenceCurrencyUuid", "yhjMzLPhuIDl");
        queryParams.put("timePeriod", timePeriod);
        queryParams.put("tiers%5B0%5D", "1");
        queryParams.put("orderBy", orderBy);
        queryParams.put("orderDirection", orderDirection);
        queryParams.put("limit", limit);
        queryParams.put("timePeriod", timePeriod);
        JsonNode dataNode = executeHttpRequest(pathSegments, queryParams,"/data/coins");

        dataNode.forEach(coinNode -> {
            CoinDto coin = mapper.convertValue(coinNode, CoinDto.class);
            coin.setVolume(coinNode.at("/24hVolume").asText());
            coinData.add(coin);
        });

        return CoinDashboardResponse.builder()
                .coins(coinData)
                .build();
    }

    @Override
    public CoinResponse getCoin(String id, String timePeriod) throws IOException {
        String[] pathSegments = {"coin", id};
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("timePeriod", timePeriod);
        JsonNode dataNode = executeHttpRequest(pathSegments, queryParams,"/data/coin");
        System.out.println(dataNode.toString());

        CoinResponse coinResponse = mapper.convertValue(dataNode, CoinResponse.class);
        coinResponse.setVolume(dataNode.at("/24hVolume").asText());

        return coinResponse;
    }

    @Override
    public GlobalStatisticsResponse getStats() throws IOException {
        String[] pathSegments = {"stats"};
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("referenceCurrencyUuid", "yhjMzLPhuIDl");
        JsonNode dataNode = executeHttpRequest(pathSegments, queryParams,"/data");

        GlobalStatisticsResponse statistics = mapper.convertValue(dataNode, GlobalStatisticsResponse.class);
        return statistics;
    }

    @Override
    public PriceHistory getCoinHistory(String id, String timePeriod) throws IOException {
        String[] pathSegments = {"coin", id, "history"};
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("referenceCurrencyUuid", "yhjMzLPhuIDl");
        queryParams.put("timePeriod", timePeriod);
        JsonNode dataNode = executeHttpRequest(pathSegments, queryParams,"/data");

        PriceHistory priceHistory = mapper.convertValue(dataNode, PriceHistory.class);
        return priceHistory;
    }

    private JsonNode executeHttpRequest(String[] pathSegments, Map<String, String> queryParams, String at) throws IOException {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(apiUrl).newBuilder();

        for (String pathSegment : pathSegments) {
            urlBuilder.addPathSegment(pathSegment);
        }

        if (queryParams != null) {
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .get()
                .addHeader("X-RapidAPI-Key", apiKey)
                .addHeader("X-RapidAPI-Host", apiHost)
                .build();

        String response = client.newCall(request).execute().body().string();
        return mapper.readTree(response).at(at);
    }
}

