package fr.eni.bookhub_back.dashboard;

import fr.eni.bookhub_back.user.bo.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class DashboardLoanDTO {
    private LocalDate dateLoaned;
    private String bookTitle;
    private User user;

}
