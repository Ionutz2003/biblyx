package com.ionut.licenta.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class LoanDTO {
    private Long id;
    private String bookTitle;
    private String bookAuthor;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status;
    private Double penalty;
}