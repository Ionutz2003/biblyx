package com.ionut.licenta.repository;

import com.ionut.licenta.entity.BookSuggestion;
import com.ionut.licenta.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookSuggestionRepository extends JpaRepository<BookSuggestion, Long> {
    List<BookSuggestion> findByUser(User user);
    List<BookSuggestion> findByStatus(BookSuggestion.SuggestionStatus status);
    List<BookSuggestion> findAllByOrderByCreatedAtDesc();
}