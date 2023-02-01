package uz.pdp.springsecurityatm.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "atm_history")
public class ATMHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double summa;   // sumda qancha miqdor yechilganligi

    private Double dollar;  // dollarda qancha miqdor yechilganligi

    @ManyToOne(optional = false)
    private ATM atm;

    @ManyToOne(optional = false)
    private Card card;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    public ATMHistory(Double summa, Double dollar, ATM atm, Card card) {
        this.summa = summa;
        this.dollar = dollar;
        this.atm = atm;
        this.card = card;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ATMHistory that = (ATMHistory) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
