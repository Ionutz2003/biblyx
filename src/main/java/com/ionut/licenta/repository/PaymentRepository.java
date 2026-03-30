package com.ionut.licenta.repository;

import com.ionut.licenta.entity.Payment;
import com.ionut.licenta.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUser(User user);
}