package uz.pdp.springsecurityatm.payload;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class TakeOutDto {
    @NotNull
    @Size(min = 3, max = 3)
    private String moneyType;   // "uzs" | "usd"
    @NotNull
    @Min(0)
    private Double money;
}
