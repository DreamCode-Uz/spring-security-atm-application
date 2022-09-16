package uz.pdp.springsecurityatm.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class CardManagerDTO extends CardDTO {
    @NotNull
    @Email
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CardManagerDTO)) return false;
        if (!super.equals(o)) return false;
        CardManagerDTO that = (CardManagerDTO) o;
        return Objects.equals(getEmail(), that.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getEmail());
    }
}
