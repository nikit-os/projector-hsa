package projector.homework4.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import projector.homework4.dto.CoinApiCurrencyRate;
import projector.homework4.dto.CurrencyRateUpdateEvent;
import projector.homework4.dto.GampPayload;
import projector.homework4.properties.GampProperties;

import java.util.List;
import java.util.Map;

@Service
public class CurrencyRateSender {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyRateFetcher.class);

    @Autowired
    private GampProperties gampProperties;

    @Autowired
    private RestTemplate restTemplate;

    public void send(CoinApiCurrencyRate coinApiCurrencyRate) {
        logger.info("Trying to send event to GAMP...");
        var event = CurrencyRateUpdateEvent.fromCoinApiCurrencyRate(coinApiCurrencyRate);
        var gampPayload = new GampPayload(gampProperties.clientId, List.of(event));

        var uriVars = Map.of(
                "api_secret", gampProperties.apiSecret,
                "measurement_id", gampProperties.measurementId
        );

        var resp = restTemplate.postForEntity(gampProperties.url, gampPayload, Void.class, uriVars);
        logger.info("Response from GAMP: {}", resp);
    }
}
