package br.com.trackflow.order.service.order.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.trackflow.order.service.order.entity.OrderStatusHistory;

public interface OrderStatusHistoryRepository
        extends JpaRepository<OrderStatusHistory, UUID> {

    Page<OrderStatusHistory> findByOrder_IdOrderByOccurredAtDesc(
            UUID orderId,
            Pageable pageable);
}
