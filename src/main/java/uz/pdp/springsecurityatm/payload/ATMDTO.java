package uz.pdp.springsecurityatm.payload;

import lombok.Data;
import uz.pdp.springsecurityatm.entity.Dollar;
import uz.pdp.springsecurityatm.entity.Summa;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@Data
public class ATMDTO {
    @NotBlank
    private String street;
    @NotBlank
    private String city;
    @NotBlank
    private String district;
    @NotNull
    private Integer bankId;
    @NotNull
    @Min(0)
    private Double commission;
    @NotNull
    private UUID userId;
    private Set<Summa> sums;
    private Set<Dollar> dollars;
}
