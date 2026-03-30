package com.ionut.licenta.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectionDTO {
    private Long id;
    private String bookTitle;
    private String bookAuthor;
    private String coverUrl;
    private String type;
    private LocalDateTime addedAt;
}