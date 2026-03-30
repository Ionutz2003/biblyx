package com.ionut.licenta.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduledTaskService {

    private final LoanService loanService;

    // Rulează în fiecare zi la ora 00:00
    @Scheduled(cron = "0 0 0 * * *")
    public void checkOverdueLoans() {
        loanService.checkOverdueLoans();
        System.out.println("✅ Verificare împrumuturi expirate efectuată!");
    }
}