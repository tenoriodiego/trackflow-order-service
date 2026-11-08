package br.com.trackflow.order.service.order.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

import br.com.trackflow.order.service.order.enums.OrderStatus;

@Entity
@Table(name = "order_status_history")
public class OrderStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false, foreignKey = @ForeignKey(name = "fk_order_history_order"))
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderStatus status;

    @Column(length = 500)
    private String description;

    @Column(length = 150)
    private String location;

    @Column(name = "occurred_at", nullable = false, updatable = false)
    private Instant occurredAt;

    protected OrderStatusHistory() {
    }

    public OrderStatusHistory(
            Order order,
            OrderStatus status,
            String description,
            String location) {

        this.order = order;
        this.status = status;
        this.description = description;
        this.location = location;
        this.occurredAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }
}
