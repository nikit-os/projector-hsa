package projector.homework4.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CurrencyRateUpdateEvent implements Serializable {

    private String name;

    private CurrencyRateUpdateParams params;

    public static CurrencyRateUpdateEvent fromCoinApiCurrencyRate(CoinApiCurrencyRate coinApiCurrencyRate) {
        var params = new CurrencyRateUpdateParams(
                coinApiCurrencyRate.getTime(),
                String.format("%s/%s", coinApiCurrencyRate.getAssetIdBase(), coinApiCurrencyRate.getAssetIdQuote()),
                coinApiCurrencyRate.getRate()
        );

        return new CurrencyRateUpdateEvent("currency_update", params);
    }
}


