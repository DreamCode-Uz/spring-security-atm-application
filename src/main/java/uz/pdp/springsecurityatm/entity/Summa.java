package uz.pdp.springsecurityatm.entity;

import lombok.*;
import org.hibernate.Hibernate;
import uz.pdp.springsecurityatm.entity.enums.UZS;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "summa")
public class Summa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private UZS uzs;

    private Integer count;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Summa summa = (Summa) o;
        return id != null && Objects.equals(id, summa.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
