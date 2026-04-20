package fr.eni.bookhub_back.user;

import fr.eni.bookhub_back.borrow.Borrow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Document(collection="user")
public class User {
    private String username;
    private String password;
    private String email;
    private String firstname;
    private String lastname;
    private Role role;
    private List<Borrow> borrowed;
}
