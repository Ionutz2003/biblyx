package com.ionut.licenta.service;

import com.ionut.licenta.dto.BookSuggestionDTO;
import com.ionut.licenta.entity.BookSuggestion;
import com.ionut.licenta.entity.User;
import com.ionut.licenta.repository.BookSuggestionRepository;
import com.ionut.licenta.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookSuggestionService {

    private final BookSuggestionRepository suggestionRepository;
    private final UserRepository userRepository;

    public BookSuggestionDTO createSuggestion(Long userId, String title,
                                              String author, String isbn, String reason) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        BookSuggestion suggestion = new BookSuggestion();
        suggestion.setUser(user);
        suggestion.setTitle(title);
        suggestion.setAuthor(author);
        suggestion.setIsbn(isbn);
        suggestion.setReason(reason);
        return convertToDTO(suggestionRepository.save(suggestion));
    }

    public List<BookSuggestionDTO> getUserSuggestions(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return suggestionRepository.findByUser(user)
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<BookSuggestionDTO> getAllSuggestions() {
        return suggestionRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public BookSuggestionDTO updateStatus(Long id, String status, String adminNote) {
        BookSuggestion suggestion = suggestionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Suggestion not found"));
        suggestion.setStatus(BookSuggestion.SuggestionStatus.valueOf(status.toUpperCase()));
        suggestion.setAdminNote(adminNote);
        return convertToDTO(suggestionRepository.save(suggestion));
    }

    private BookSuggestionDTO convertToDTO(BookSuggestion s) {
        return new BookSuggestionDTO(
                s.getId(),
                s.getUser().getUsername(),
                s.getTitle(),
                s.getAuthor(),
                s.getIsbn(),
                s.getReason(),
                s.getStatus().name(),
                s.getAdminNote(),
                s.getCreatedAt()
        );
    }
}