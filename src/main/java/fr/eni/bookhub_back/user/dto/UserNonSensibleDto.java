package fr.eni.bookhub_back.user.dto;

import fr.eni.bookhub_back.user.bo.Role;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"username"})
public class UserNonSensibleDto {
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private Role role ;
}