package projector.homework4.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gamp")
@Data
public class GampProperties {

    public String url;
    public String clientId;
    public String apiSecret;
    public String measurementId;

}
