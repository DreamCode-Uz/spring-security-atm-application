package uz.pdp.springsecurityatm.payload;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class CardDTO {
    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    @Pattern(regexp = "^[0-9]{4}$")
    private String pinCode;
    @NotNull
    @Min(0)
    private Double balance;
    @NotNull
    private Integer cardId;

}
