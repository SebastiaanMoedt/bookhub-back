package fr.eni.bookhub_back.user;

import fr.eni.bookhub_back.loan.Loan;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"username"})

@Entity
@Table(name = "APP_USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "{user.username.blank-error}")
    @Size(min = 3, message = "{user.username.size-error}")
    @Column(name = "USERNAME", nullable = false)
    private String username;

    @NotBlank(message = "{user.password.blank-error}")
    @Size(min = 12, message = "{user.password.size-error}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).+$", message = "{user.password.pattern-error}")
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @NotBlank(message = "{user.email.blank-error}")
    @Email(message = "{user.email.pattern-error}")
    @Size(max = 255)
    @Column(name = "EMAIL", length = 255, nullable = false, unique = true) // unique = true pour les clés secondaires
    private String email;

    @NotBlank(message = "{user.firstname.blank-error}")
    @Size(min = 3, max = 150, message = "{user.firstname.size-error}")
    @Column(name = "FIRSTNAME", length = 150, nullable = false)

    private String firstname;

    @NotBlank(message = "{user.lastname.blank-error}")
    @Size(min = 3, max = 90, message = "{user.lastname.size-error}")
    @Column(name = "LASTNAME", length = 90, nullable = false)
    private String lastname;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
    private @Builder.Default Role role = Role.USER;

    @OneToMany(mappedBy = "user")
    private @Builder.Default List<Loan> loans = new ArrayList<>();
}