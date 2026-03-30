package com.ionut.licenta.controller;

import com.ionut.licenta.entity.Book;
import com.ionut.licenta.service.OpenLibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class OpenLibraryController {

    private final OpenLibraryService openLibraryService;

    @PostMapping("/import")
    public ResponseEntity<?> importBooks(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            List<Book> books = openLibraryService.importBooks(query, limit);
            return ResponseEntity.ok(
                    "✅ Importate " + books.size() + " cărți pentru: " + query);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("❌ Eroare: " + e.getMessage());
        }
    }
}