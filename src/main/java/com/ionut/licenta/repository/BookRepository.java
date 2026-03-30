package com.ionut.licenta.repository;

import com.ionut.licenta.entity.Book;
import com.ionut.licenta.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Book> findByCategory(Category category, Pageable pageable);
    Page<Book> findByTitleContainingIgnoreCaseAndCategory(
            String title, Category category, Pageable pageable);
}