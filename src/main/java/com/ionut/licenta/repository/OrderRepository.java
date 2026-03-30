package com.ionut.licenta.repository;

import com.ionut.licenta.entity.Order;
import com.ionut.licenta.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}