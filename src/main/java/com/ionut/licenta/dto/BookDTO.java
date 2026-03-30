package com.ionut.licenta.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String description;
    private Double price;
    private String coverUrl;
    private Integer stock;
    private String categoryName;
    private Double averageRating;
    private Integer reviewCount;
}