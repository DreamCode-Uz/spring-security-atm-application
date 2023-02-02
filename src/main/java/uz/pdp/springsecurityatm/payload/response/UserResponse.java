package uz.pdp.springsecurityatm.payload.response;

import lombok.Data;
import uz.pdp.springsecurityatm.entity.Role;
import uz.pdp.springsecurityatm.entity.User;

import java.util.Set;
import java.util.UUID;

@Data
public class UserResponse {
    private UUID id;
    private String firstname;
    private String lastname;
    private Set<Role> roles;
    private Set<CardResponse> cards;

    public UserResponse(User user, Set<CardResponse> cardResponses) {
        this.id = user.getId();
        this.firstname = user.getFirstName();
        this.lastname = user.getLastname();
        this.roles = user.getRoles();
        this.cards = cardResponses;
    }
}
