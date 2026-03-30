package com.ionut.licenta.service;

import com.ionut.licenta.dto.BookDTO;
import com.ionut.licenta.entity.Book;
import com.ionut.licenta.entity.Category;
import com.ionut.licenta.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final ReviewRepository reviewRepository;
    private final CollectionRepository collectionRepository;
    private final LoanRepository loanRepository;
    private final StockRepository stockRepository;

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        return convertToDTO(book);
    }

    public Page<BookDTO> searchBooks(String title, Long categoryId, Pageable pageable) {
        Page<Book> books;
        if (title != null && categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            books = bookRepository.findByTitleContainingIgnoreCaseAndCategory(
                    title, category, pageable);
        } else if (title != null) {
            books = bookRepository.findByTitleContainingIgnoreCase(title, pageable);
        } else if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            books = bookRepository.findByCategory(category, pageable);
        } else {
            books = bookRepository.findAll(pageable);
        }
        return books.map(this::convertToDTO);
    }

    public Book updateBook(Long id, Book updatedBook) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        book.setTitle(updatedBook.getTitle());
        book.setAuthor(updatedBook.getAuthor());
        book.setDescription(updatedBook.getDescription());
        book.setPrice(updatedBook.getPrice());
        book.setStock(updatedBook.getStock());
        book.setCoverUrl(updatedBook.getCoverUrl());
        book.setCategory(updatedBook.getCategory());
        return bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        reviewRepository.deleteAll(reviewRepository.findByBook(book));

        collectionRepository.deleteAll(
                collectionRepository.findAll().stream()
                        .filter(c -> c.getBook().getId().equals(id))
                        .toList()
        );

        loanRepository.deleteAll(
                loanRepository.findAll().stream()
                        .filter(l -> l.getBook().getId().equals(id))
                        .toList()
        );

        stockRepository.findByBook(book)
                .ifPresent(stockRepository::delete);

        bookRepository.deleteById(id);
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<BookDTO> getSimilarBooks(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        if (book.getCategory() == null) return List.of();
        return bookRepository.findByCategory(book.getCategory(),
                        Pageable.ofSize(5)).getContent()
                .stream()
                .filter(b -> !b.getId().equals(bookId))
                .map(this::convertToDTO)
                .toList();
    }

    public BookDTO convertToDTO(Book book) {
        Double avgRating = reviewRepository.findAverageRatingByBook(book);
        long reviewCount = reviewRepository.findByBook(book).size();
        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getDescription(),
                book.getPrice(),
                book.getCoverUrl(),
                book.getStock(),
                book.getCategory() != null ? book.getCategory().getName() : null,
                avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : null,
                (int) reviewCount
        );
    }
}