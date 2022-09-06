package uz.pdp.springsecurityatm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Card implements UserDetails {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String cardNumber;

    @Column
    private String cvv;

    @Column(nullable = false)
    @JsonIgnore
    private String pinCode;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expireDate;

    @Transient
    private String fullName;

    @ManyToOne(optional = false)
    private Bank bank;

    @ManyToOne(optional = false)
    private User user;

    private Double balance = 0D;

    private boolean enabled;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.user.getRoles();
    }

    @Override
    public String getPassword() {
        return this.pinCode;
    }

    @Override
    public String getUsername() {
        return this.cardNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public String getFullName() {
        return String.format("%s %s", this.user.getFirstName(), this.user.getLastname());
    }

    public Card(String cardNumber, String cvv, String pinCode, Bank bank, User user, Double balance) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.pinCode = pinCode;
        this.bank = bank;
        this.user = user;
        this.balance = balance;
    }
}

//    @Pattern(regexp = "^[0-9]{3}$")
