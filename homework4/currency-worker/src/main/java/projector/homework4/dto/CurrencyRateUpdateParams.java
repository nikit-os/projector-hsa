package projector.homework4.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CurrencyRateUpdateParams implements Serializable {

    private String time;

    private String pair;

    private BigDecimal rate;

}


