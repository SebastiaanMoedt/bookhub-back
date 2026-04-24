package fr.eni.bookhub_back.user.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"username"})
public class AuthUserDto {
    private String username;
    private String password;
}