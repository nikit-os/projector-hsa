package projector.homework4.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CoinApiCurrencyRate implements Serializable {

    private String time;

    @JsonProperty("asset_id_base")
    private String assetIdBase;

    @JsonProperty("asset_id_quote")
    private String assetIdQuote;

    private BigDecimal rate;

}
