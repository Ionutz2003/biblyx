package com.ionut.licenta.controller;

import com.ionut.licenta.dto.LoanDTO;
import com.ionut.licenta.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    public ResponseEntity<LoanDTO> createLoan(@RequestBody Map<String, Object> body) {
        Long userId = Long.valueOf(body.get("userId").toString());
        Long bookId = Long.valueOf(body.get("bookId").toString());
        return ResponseEntity.ok(loanService.createLoan(userId, bookId));
    }

    @PutMapping("/{loanId}/return")
    public ResponseEntity<LoanDTO> returnLoan(@PathVariable Long loanId) {
        return ResponseEntity.ok(loanService.returnLoan(loanId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LoanDTO>> getUserLoans(@PathVariable Long userId) {
        return ResponseEntity.ok(loanService.getUserLoans(userId));
    }

    @GetMapping("/user/{userId}/active")
    public ResponseEntity<List<LoanDTO>> getActiveLoans(@PathVariable Long userId) {
        return ResponseEntity.ok(loanService.getActiveLoans(userId));
    }

    @GetMapping("/admin/all")
    public ResponseEntity<List<LoanDTO>> getAllLoans() {
        return ResponseEntity.ok(loanService.getAllLoans());
    }
}