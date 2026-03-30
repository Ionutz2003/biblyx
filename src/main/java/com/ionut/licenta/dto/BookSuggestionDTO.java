package com.ionut.licenta.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookSuggestionDTO {
    private Long id;
    private String username;
    private String title;
    private String author;
    private String isbn;
    private String reason;
    private String status;
    private String adminNote;
    private LocalDateTime createdAt;
}