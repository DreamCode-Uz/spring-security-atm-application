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
public class AtmDTO {
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
    private UUID userId; // Ushbu bankomatga javobgar shaxsning id raqami
    private Double takeOut; // pul yechib olishning maximal miqdori kiritiladi
    private Set<Summa> sums;
    private Set<Dollar> dollars;
    private Set<Integer> cardTypes; //  pul yechib olish uchun ruxsat etilgan karta tiplari kiritiladi
}
