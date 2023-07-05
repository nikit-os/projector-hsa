package projector.homework4.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "coin-api")
@Data
public class CoinApiProperties {

    public String url;
    public String token;
    public String assetBase;
    public String assetQuote;

}
