package projector.homework4.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GampPayload {

    @JsonProperty("client_id")
    private String clientId;

    private List<CurrencyRateUpdateEvent> events;
}
