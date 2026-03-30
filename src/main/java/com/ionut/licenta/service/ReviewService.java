package com.ionut.licenta.service;

import com.ionut.licenta.dto.ReviewDTO;
import com.ionut.licenta.entity.Book;
import com.ionut.licenta.entity.Review;
import com.ionut.licenta.entity.User;
import com.ionut.licenta.repository.BookRepository;
import com.ionut.licenta.repository.ReviewRepository;
import com.ionut.licenta.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public ReviewDTO addReview(Long userId, Long bookId, Integer rating, String content) {
        if (rating < 1 || rating > 5)
            throw new RuntimeException("Rating must be between 1 and 5!");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (reviewRepository.existsByUserAndBook(user, book))
            throw new RuntimeException("You already reviewed this book!");

        Review review = new Review();
        review.setUser(user);
        review.setBook(book);
        review.setRating(rating);
        review.setContent(content);
        return convertToDTO(reviewRepository.save(review));
    }

    public List<ReviewDTO> getReviewsByBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        return reviewRepository.findByBook(book)
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Double getAverageRating(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        return reviewRepository.findAverageRatingByBook(book);
    }

    private ReviewDTO convertToDTO(Review review) {
        return new ReviewDTO(
                review.getId(),
                review.getUser().getUsername(),
                review.getBook().getTitle(),
                review.getRating(),
                review.getContent(),
                review.getCreatedAt()
        );
    }
}
