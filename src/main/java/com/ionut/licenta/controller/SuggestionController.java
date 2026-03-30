package com.ionut.licenta.controller;

import com.ionut.licenta.dto.BookSuggestionDTO;
import com.ionut.licenta.service.BookSuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/suggestions")
@RequiredArgsConstructor
public class SuggestionController {

    private final BookSuggestionService bookSuggestionService;

    @PostMapping
    public ResponseEntity<BookSuggestionDTO> createSuggestion(
            @RequestBody Map<String, Object> body) {
        Long userId = Long.valueOf(body.get("userId").toString());
        String title = body.get("title").toString();
        String author = body.getOrDefault("author", "").toString();
        String isbn = body.getOrDefault("isbn", "").toString();
        String reason = body.getOrDefault("reason", "").toString();
        return ResponseEntity.ok(
                bookSuggestionService.createSuggestion(userId, title, author, isbn, reason));
    }

    @GetMapping("/my/{userId}")
    public ResponseEntity<List<BookSuggestionDTO>> getMySuggestions(
            @PathVariable Long userId) {
        return ResponseEntity.ok(bookSuggestionService.getUserSuggestions(userId));
    }

    @GetMapping("/admin")
    public ResponseEntity<List<BookSuggestionDTO>> getAllSuggestions() {
        return ResponseEntity.ok(bookSuggestionService.getAllSuggestions());
    }

    @PutMapping("/admin/{id}/status")
    public ResponseEntity<BookSuggestionDTO> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {
        String status = body.get("status").toString();
        String adminNote = body.getOrDefault("adminNote", "").toString();
        return ResponseEntity.ok(
                bookSuggestionService.updateStatus(id, status, adminNote));
    }
}