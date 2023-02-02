package uz.pdp.springsecurityatm.entity;

import lombok.*;
import org.hibernate.Hibernate;
import uz.pdp.springsecurityatm.entity.enums.USD;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

/*******************************
 *   @author Dilshod Fayzullayev
 ********************************/

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Dollar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private USD usd;

    private Integer count;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Dollar dollar = (Dollar) o;
        return id != null && Objects.equals(id, dollar.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
