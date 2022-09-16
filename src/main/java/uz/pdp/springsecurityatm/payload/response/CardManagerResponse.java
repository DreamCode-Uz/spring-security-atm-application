package uz.pdp.springsecurityatm.payload.response;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurityatm.entity.Card;

@AllArgsConstructor
@NoArgsConstructor
public class CardManagerResponse extends CardResponse {
    private String email;

    public CardManagerResponse(Card card) {
        super(card);
        this.email = card.getUser().getBankOwner().getEmail();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
