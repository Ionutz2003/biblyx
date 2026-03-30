package com.ionut.licenta.repository;

import com.ionut.licenta.entity.Book;
import com.ionut.licenta.entity.Review;
import com.ionut.licenta.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByBook(Book book);
    List<Review> findByUser(User user);
    boolean existsByUserAndBook(User user, Book book);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.book = :book")
    Double findAverageRatingByBook(Book book);
}