package com.ionut.licenta.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private Long id;
    private String username;
    private String bookTitle;
    private Integer rating;
    private String content;
    private LocalDateTime createdAt;
}