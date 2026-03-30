package com.ionut.licenta.controller;

import com.ionut.licenta.dto.CollectionDTO;
import com.ionut.licenta.service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/collections")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;

    @PostMapping
    public ResponseEntity<CollectionDTO> addToCollection(@RequestBody Map<String, Object> body) {
        Long userId = Long.valueOf(body.get("userId").toString());
        Long bookId = Long.valueOf(body.get("bookId").toString());
        String type = body.get("type").toString();
        return ResponseEntity.ok(collectionService.addToCollection(userId, bookId, type));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CollectionDTO>> getUserCollection(
            @PathVariable Long userId,
            @RequestParam(required = false) String type) {
        return ResponseEntity.ok(collectionService.getUserCollection(userId, type));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFromCollection(@PathVariable Long id) {
        collectionService.removeFromCollection(id);
        return ResponseEntity.noContent().build();
    }
}