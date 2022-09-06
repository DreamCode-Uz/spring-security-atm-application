package uz.pdp.springsecurityatm.payload.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurityatm.entity.Bank;
import uz.pdp.springsecurityatm.entity.Card;

import java.util.UUID;

@NoArgsConstructor
@Data
public class CardResponse {
    private UUID cardId;
    private String fullName;
    private String cardNumber;
    private String cvv;
    private Double balance;
    private boolean enabled;
    private boolean accountNonLocked;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private UUID userId;
    private Bank bank;

    public CardResponse(Card card) {
        this.cardId = card.getId();
        this.fullName = card.getFullName();
        this.cardNumber = card.getCardNumber();
        this.cvv = card.getCvv();
        this.balance = card.getBalance();
        this.enabled = card.isEnabled();
        this.accountNonLocked = card.isAccountNonLocked();
        this.accountNonExpired = card.isAccountNonExpired();
        this.credentialsNonExpired = card.isCredentialsNonExpired();
        this.userId = card.getUser().getId();
        this.bank = card.getBank();
    }
}
