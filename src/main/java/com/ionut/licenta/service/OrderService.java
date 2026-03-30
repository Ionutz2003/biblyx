package com.ionut.licenta.service;

import com.ionut.licenta.dto.OrderDTO;
import com.ionut.licenta.dto.OrderItemDTO;
import com.ionut.licenta.entity.*;
import com.ionut.licenta.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final StockRepository stockRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public OrderDTO checkout(Long userId, Map<Long, Integer> bookQuantities) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<OrderItem> items = new ArrayList<>();
        double total = 0.0;

        for (Map.Entry<Long, Integer> entry : bookQuantities.entrySet()) {
            Book book = bookRepository.findById(entry.getKey())
                    .orElseThrow(() -> new RuntimeException("Book not found"));

            int quantity = entry.getValue();
            double price;

            // Încearcă să găsească stocul în tabela stock
            // Dacă nu există, folosește datele direct de pe carte
            var stockOpt = stockRepository.findByBook(book);
            if (stockOpt.isPresent()) {
                Stock stock = stockOpt.get();
                if (stock.getQuantity() < quantity)
                    throw new RuntimeException("Stoc insuficient pentru: " + book.getTitle());
                stock.setQuantity(stock.getQuantity() - quantity);
                stockRepository.save(stock);
                price = stock.getPrice();
            } else {
                // Folosește datele direct de pe carte (importate din Open Library)
                if (book.getStock() == null || book.getStock() < quantity)
                    throw new RuntimeException("Stoc insuficient pentru: " + book.getTitle());
                book.setStock(book.getStock() - quantity);
                bookRepository.save(book);
                price = book.getPrice() != null ? book.getPrice() : 0.0;
            }

            OrderItem item = new OrderItem();
            item.setBook(book);
            item.setQuantity(quantity);
            item.setPrice(price);
            items.add(item);
            total += price * quantity;
        }

        Order order = new Order();
        order.setUser(user);
        order.setTotalPrice(total);
        order.setStatus(Order.OrderStatus.CONFIRMED);
        Order savedOrder = orderRepository.save(order);

        items.forEach(item -> item.setOrder(savedOrder));
        savedOrder.setItems(items);
        orderRepository.save(savedOrder);

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setAmount(total);
        payment.setStatus(Payment.PaymentStatus.COMPLETED);
        paymentRepository.save(payment);

        return convertToDTO(savedOrder);
    }

    public List<OrderDTO> getUserOrders(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUser(user)
                .stream().map(this::convertToDTO).collect(Collectors.toList());

    }
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private OrderDTO convertToDTO(Order order) {
        List<OrderItemDTO> itemDTOs = order.getItems() == null ? new ArrayList<>() :
                order.getItems().stream().map(item -> new OrderItemDTO(
                        item.getBook().getId(),
                        item.getBook().getTitle(),
                        item.getQuantity(),
                        item.getPrice()
                )).collect(Collectors.toList());

        return new OrderDTO(
                order.getId(),
                order.getUser().getUsername(),
                itemDTOs,
                order.getTotalPrice(),
                order.getStatus().name(),
                order.getCreatedAt()
        );
    }
}