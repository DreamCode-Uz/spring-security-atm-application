package uz.pdp.springsecurityatm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class ATM {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_type", nullable = false)
    @ManyToMany
    @ToString.Exclude
    private Set<CardType> cardType;

    @ManyToOne
    private Bank bank;

    @OneToOne
    private Address address;

    @ManyToMany
    @ToString.Exclude
    private Set<Summa> summas;

    @ManyToMany
    @ToString.Exclude
    private Set<Dollar> dollars;

    //    @ManyToOne(fetch = FetchType.LAZY)
////    @JsonIgnore
    @Column(name = "user_id", nullable = false)
    private UUID userId;  //  Masul hodim
    private Double balance = 0D;
    private Double commission = 0D;
    private Double maxWithdraw;

    public ATM(Set<CardType> cardType, Bank bank, Address address, Set<Summa> summas, Set<Dollar> dollars, UUID userId, Double balance, Double commission, Double maxWithdraw) {
        this.cardType = cardType;
        this.bank = bank;
        this.address = address;
        this.summas = summas;
        this.dollars = dollars;
        this.userId = userId;
        this.balance = balance;
        this.commission = commission;
        this.maxWithdraw = maxWithdraw;
    }

}
