package com.ionut.licenta.repository;

import com.ionut.licenta.entity.Collection;
import com.ionut.licenta.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
    List<Collection> findByUser(User user);
    List<Collection> findByUserAndType(User user, Collection.CollectionType type);
    Optional<Collection> findByUserAndBook(
            com.ionut.licenta.entity.User user,
            com.ionut.licenta.entity.Book book);
    boolean existsByUserAndBook(
            com.ionut.licenta.entity.User user,
            com.ionut.licenta.entity.Book book);
}