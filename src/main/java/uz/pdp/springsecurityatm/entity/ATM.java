package uz.pdp.springsecurityatm.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
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
    private Set<Summa> summas;

    @ManyToMany
    private Set<Dollar> dollars;

    @ManyToOne
    private User user;  //  Masul hodim

    private Double balance = 0D;
    private Double commission = 0D;
    private Double maxWithdraw;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ATM atm = (ATM) o;
        return id != null && Objects.equals(id, atm.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
