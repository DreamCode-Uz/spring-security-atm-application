package uz.pdp.springsecurityatm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

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

    @ManyToOne
    private User user;  //  Masul hodim
    private Double balance = 0D;
    private Double commission = 0D;
    private Double maxWithdraw;

    public ATM(Set<CardType> cardType, Bank bank, Address address, Set<Summa> summas, Set<Dollar> dollars, User user, Double balance, Double commission, Double maxWithdraw) {
        this.cardType = cardType;
        this.bank = bank;
        this.address = address;
        this.summas = summas;
        this.dollars = dollars;
        this.user = user;
        this.balance = balance;
        this.commission = commission;
        this.maxWithdraw = maxWithdraw;
    }

}
