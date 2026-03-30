package com.ionut.licenta.service;

import com.ionut.licenta.dto.CollectionDTO;
import com.ionut.licenta.entity.Book;
import com.ionut.licenta.entity.Collection;
import com.ionut.licenta.entity.User;
import com.ionut.licenta.repository.BookRepository;
import com.ionut.licenta.repository.CollectionRepository;
import com.ionut.licenta.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public CollectionDTO addToCollection(Long userId, Long bookId, String type) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (collectionRepository.existsByUserAndBook(user, book))
            throw new RuntimeException("Book already in collection!");

        Collection collection = new Collection();
        collection.setUser(user);
        collection.setBook(book);
        collection.setType(Collection.CollectionType.valueOf(type.toUpperCase()));
        return convertToDTO(collectionRepository.save(collection));
    }

    public List<CollectionDTO> getUserCollection(Long userId, String type) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Collection> collections = type != null
                ? collectionRepository.findByUserAndType(
                user, Collection.CollectionType.valueOf(type.toUpperCase()))
                : collectionRepository.findByUser(user);
        return collections.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public void removeFromCollection(Long collectionId) {
        collectionRepository.deleteById(collectionId);
    }

    private CollectionDTO convertToDTO(Collection collection) {
        return new CollectionDTO(
                collection.getId(),
                collection.getBook().getTitle(),
                collection.getBook().getAuthor(),
                collection.getBook().getCoverUrl(),
                collection.getType().name(),
                collection.getAddedAt()
        );
    }
}