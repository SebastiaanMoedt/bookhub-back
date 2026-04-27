package fr.eni.bookhub_back.review;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDTO {
        String comment;
        int score;
        String bookISBN;
        String username;
    }
