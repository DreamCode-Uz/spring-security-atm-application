package uz.pdp.springsecurityatm.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class LoginDTO {
    @NotNull(message = "Card number must be entered")
    @Pattern(regexp = "^[0-9]{16}$", message = "Card number must be 16 digits")
    private String cardNumber;
    @NotNull(message = "Pin code must be entered")
    @Pattern(regexp = "^[0-9]{4}$", message = "Pin code must consist of 4 characters")
    private String pinCode;
}
