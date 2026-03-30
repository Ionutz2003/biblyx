package com.ionut.licenta.controller;

import com.ionut.licenta.dto.OrderDTO;
import com.ionut.licenta.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<OrderDTO> checkout(@RequestBody Map<String, Object> body) {
        Long userId = Long.valueOf(body.get("userId").toString());


        Map<String, Object> rawItems = (Map<String, Object>) body.get("items");
        Map<Long, Integer> items = new HashMap<>();
        for (Map.Entry<String, Object> entry : rawItems.entrySet()) {
            items.put(Long.valueOf(entry.getKey()),
                    Integer.valueOf(entry.getValue().toString()));
        }

        return ResponseEntity.ok(orderService.checkout(userId, items));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getUserOrders(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getUserOrders(userId));
    }
    @GetMapping("/admin/all")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
}