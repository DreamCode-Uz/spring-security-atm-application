package uz.pdp.springsecurityatm.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
//  @Data => Using @Data for JPA entities is not recommended. It can cause severe performance and memory consumption issues.
@Getter
@Setter
@ToString
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastname;

    @OneToMany
    @ToString.Exclude
    private Set<Role> roles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
