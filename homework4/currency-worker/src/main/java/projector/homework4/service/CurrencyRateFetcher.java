package projector.homework4.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;
import projector.homework4.dto.CoinApiCurrencyRate;
import projector.homework4.properties.CoinApiProperties;

import java.util.Map;

@Service
public class CurrencyRateFetcher {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyRateFetcher.class);

    @Autowired
    private CoinApiProperties coinApiProps;

    @Autowired
    private RestTemplate restTemplate;

    private final String COIN_API_TOKEN_HEADER = "X-CoinAPI-Key";

    public CoinApiCurrencyRate fetch() {
        logger.info("Trying to fetch curency rates...");
        var uriVars = Map.of(
                "asset_base", coinApiProps.assetBase,
                "asset_quote", coinApiProps.assetQuote
        );

        var requestEntity = RequestEntity.get(new UriTemplate(coinApiProps.url).expand(uriVars).toString())
                .header(COIN_API_TOKEN_HEADER, coinApiProps.token)
                .build();

        var currencyRate = restTemplate.exchange(requestEntity, CoinApiCurrencyRate.class);
        logger.info("Received currency rate response : {}", currencyRate);
        return currencyRate.getBody();
    }
}
