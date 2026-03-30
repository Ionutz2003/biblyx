package com.ionut.licenta.service;

import com.ionut.licenta.entity.Book;
import com.ionut.licenta.entity.Category;
import com.ionut.licenta.repository.BookRepository;
import com.ionut.licenta.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenLibraryService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public List<Book> importBooks(String query, int limit) throws Exception {
        String url = String.format(
                "https://openlibrary.org/search.json?q=%s&limit=%d",
                query.replace(" ", "+"), limit);

        String response = restTemplate.getForObject(url, String.class);
        JsonNode root = objectMapper.readTree(response);
        JsonNode docs = root.get("docs");

        List<Book> imported = new ArrayList<>();

        for (JsonNode doc : docs) {
            String isbn = null;
            if (doc.has("isbn") && doc.get("isbn").size() > 0) {
                isbn = doc.get("isbn").get(0).asText();
            }

            if (isbn != null && bookRepository.findByIsbn(isbn).isPresent()) {
                continue;
            }

            Book book = new Book();
            book.setTitle(doc.has("title")
                    ? doc.get("title").asText() : "Titlu necunoscut");
            book.setAuthor(doc.has("author_name") && doc.get("author_name").size() > 0
                    ? doc.get("author_name").get(0).asText() : "Autor necunoscut");
            book.setIsbn(isbn);
            book.setDescription(doc.has("first_sentence")
                    ? doc.get("first_sentence").get("value").asText() : null);

            String coverId = doc.has("cover_i")
                    ? doc.get("cover_i").asText() : null;
            if (coverId != null) {
                book.setCoverUrl("https://covers.openlibrary.org/b/id/"
                        + coverId + "-M.jpg");
            }

            book.setPrice(Math.round((19.99 + Math.random() * 80) * 100.0) / 100.0);
            book.setStock((int)(Math.random() * 20) + 1);

            if (doc.has("subject") && doc.get("subject").size() > 0) {
                String rawSubject = doc.get("subject").get(0).asText();
                final String subjectName = rawSubject.length() > 50
                        ? rawSubject.substring(0, 50) : rawSubject;

                Category category = categoryRepository
                        .findByName(subjectName)
                        .orElseGet(() -> {
                            Category c = new Category();
                            c.setName(subjectName);
                            return categoryRepository.save(c);
                        });
                book.setCategory(category);
                book.setCategory(category);
            }

            imported.add(bookRepository.save(book));
        }

        return imported;
    }
}