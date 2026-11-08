package br.com.trackflow.order.service.order.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.trackflow.order.service.order.entity.Order;

public interface OrderRepository
        extends JpaRepository<Order, UUID> {

    Optional<Order> findByTrackingCode(String trackingCode);

    boolean existsByTrackingCode(String trackingCode);

    Page<Order> findByCreatedBy(
            UUID createdBy,
            Pageable pageable);
}
