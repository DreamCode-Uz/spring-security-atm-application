package uz.pdp.springsecurityatm.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "bank_owner")
public class BankOwner {

    @Id
    @GeneratedValue
    private UUID id;

    private String email;

    @OneToOne(mappedBy = "bankOwner", cascade = CascadeType.ALL)
    private User user;

    @ManyToOne
    private Bank bank;
}
