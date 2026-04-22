package fr.eni.bookhub_back.user;

import fr.eni.bookhub_back.loan.Loan;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotBlank
    @Size(max = 20, message = "{user.username.size-error}")
    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @NotBlank(message = "{user.email.blank-error}")
    @Size(max = 255, message = "{user.email.size-error}")
    @Email(message = "{user.email.pattern-error}")
    @Column(name = "EMAIL", length = 255, nullable = false, unique = true) // unique = true pour les clés secondaires
    private String email;

    @NotBlank(message = "{user.firstname.blank-error}")
    @Size(max = 150, message = "{user.firstname.size-error}")
    @Column(name = "FIRSTNAME", length = 150, nullable = false)

    private String firstname;

    @NotBlank(message = "{user.lastname.blank-error}")
    @Size(max = 90, message = "{user.lastname.size-error}")
    @Column(name = "LASTNAME", length = 90, nullable = false)
    private String lastname;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
    private @Builder.Default Role role = Role.USER;

    @OneToMany(mappedBy = "user")
    private @Builder.Default List<Loan> loaned = new ArrayList<>();
}