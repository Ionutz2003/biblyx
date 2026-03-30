package com.ionut.licenta.controller;

import com.ionut.licenta.dto.ReviewDTO;
import com.ionut.licenta.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{bookId}/reviews")
    public ResponseEntity<ReviewDTO> addReview(
            @PathVariable Long bookId,
            @RequestBody Map<String, Object> body) {
        Long userId = Long.valueOf(body.get("userId").toString());
        Integer rating = Integer.valueOf(body.get("rating").toString());
        String content = body.get("content").toString();
        return ResponseEntity.ok(reviewService.addReview(userId, bookId, rating, content));
    }

    @GetMapping("/{bookId}/reviews")
    public ResponseEntity<List<ReviewDTO>> getReviews(@PathVariable Long bookId) {
        return ResponseEntity.ok(reviewService.getReviewsByBook(bookId));
    }

    @GetMapping("/{bookId}/rating")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long bookId) {
        return ResponseEntity.ok(reviewService.getAverageRating(bookId));
    }
}