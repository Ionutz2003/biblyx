package com.ionut.licenta.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long id;
    private String username;
    private List<OrderItemDTO> items;
    private Double totalPrice;
    private String status;
    private LocalDateTime createdAt;
}
