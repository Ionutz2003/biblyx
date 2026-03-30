package com.ionut.licenta.repository;

import com.ionut.licenta.entity.Stock;
import com.ionut.licenta.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByBook(Book book);
}