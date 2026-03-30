package com.ionut.licenta.repository;

import com.ionut.licenta.entity.Loan;
import com.ionut.licenta.entity.User;
import com.ionut.licenta.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUser(User user);
    List<Loan> findByUserAndStatus(User user, Loan.LoanStatus status);
    boolean existsByUserAndBookAndStatus(User user, Book book, Loan.LoanStatus status);
}