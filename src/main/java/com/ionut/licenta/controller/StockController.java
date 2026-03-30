package com.ionut.licenta.controller;

import com.ionut.licenta.entity.Book;
import com.ionut.licenta.entity.Stock;
import com.ionut.licenta.repository.BookRepository;
import com.ionut.licenta.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockRepository stockRepository;
    private final BookRepository bookRepository;

    @PostMapping
    public ResponseEntity<Stock> addStock(@RequestBody Map<String, Object> body) {
        Long bookId = Long.valueOf(body.get("bookId").toString());
        Integer quantity = Integer.valueOf(body.get("quantity").toString());
        Double price = Double.valueOf(body.get("price").toString());

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Stock stock = stockRepository.findByBook(book).orElse(new Stock());
        stock.setBook(book);
        stock.setQuantity(quantity);
        stock.setPrice(price);
        return ResponseEntity.ok(stockRepository.save(stock));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<Stock> getStock(@PathVariable Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        Stock stock = stockRepository.findByBook(book)
                .orElseThrow(() -> new RuntimeException("No stock found"));
        return ResponseEntity.ok(stock);
    }
}