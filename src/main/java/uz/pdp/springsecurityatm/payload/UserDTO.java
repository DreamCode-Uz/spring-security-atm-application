package uz.pdp.springsecurityatm.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Set;
import java.util.UUID;

@Data
public class UserDTO {
    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    private Set<Integer> rolesId;
    private Set<UUID> cardsId;
}
