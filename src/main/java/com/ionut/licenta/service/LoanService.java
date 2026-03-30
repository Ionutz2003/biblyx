package com.ionut.licenta.service;

import com.ionut.licenta.dto.LoanDTO;
import com.ionut.licenta.entity.Book;
import com.ionut.licenta.entity.Loan;
import com.ionut.licenta.entity.User;
import com.ionut.licenta.repository.BookRepository;
import com.ionut.licenta.repository.LoanRepository;
import com.ionut.licenta.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public LoanDTO createLoan(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setLoanDate(LocalDateTime.now());
        loan.setDueDate(LocalDateTime.now().plusDays(14));
        loan.setStatus(Loan.LoanStatus.ACTIVE);
        loan.setPenalty(0.0);

        return convertToDTO(loanRepository.save(loan));
    }

    public LoanDTO returnLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        loan.setReturnDate(LocalDateTime.now());
        loan.setStatus(Loan.LoanStatus.RETURNED);

        if (LocalDateTime.now().isAfter(loan.getDueDate())) {
            long daysLate = ChronoUnit.DAYS.between(loan.getDueDate(), LocalDateTime.now());
            loan.setPenalty(daysLate * 1.0);
        }

        return convertToDTO(loanRepository.save(loan));
    }

    public List<LoanDTO> getUserLoans(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return loanRepository.findByUser(user)
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<LoanDTO> getActiveLoans(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return loanRepository.findByUser(user).stream()
                .filter(l -> l.getStatus() == Loan.LoanStatus.ACTIVE)
                .map(this::convertToDTO).collect(Collectors.toList());
    }
    public List<LoanDTO> getAllLoans() {
        return loanRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    public void checkOverdueLoans() {
        loanRepository.findAll().stream()
                .filter(l -> l.getStatus() == Loan.LoanStatus.ACTIVE
                        && LocalDateTime.now().isAfter(l.getDueDate()))
                .forEach(loan -> {
                    long daysLate = ChronoUnit.DAYS.between(
                            loan.getDueDate(), LocalDateTime.now());
                    loan.setStatus(Loan.LoanStatus.OVERDUE);
                    loan.setPenalty(daysLate * 1.0);
                    loanRepository.save(loan);
                });
    }

    private LoanDTO convertToDTO(Loan loan) {
        LoanDTO dto = new LoanDTO();
        dto.setId(loan.getId());
        dto.setBookTitle(loan.getBook().getTitle());
        dto.setBookAuthor(loan.getBook().getAuthor());
        dto.setLoanDate(loan.getLoanDate() != null
                ? loan.getLoanDate().toLocalDate() : null);
        dto.setDueDate(loan.getDueDate() != null
                ? loan.getDueDate().toLocalDate() : null);
        dto.setReturnDate(loan.getReturnDate() != null
                ? loan.getReturnDate().toLocalDate() : null);
        dto.setStatus(loan.getStatus().name());
        dto.setPenalty(loan.getPenalty() != null ? loan.getPenalty() : 0.0);
        return dto;
    }
}