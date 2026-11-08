package br.com.trackflow.order.service.order.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.trackflow.order.service.address.entity.Address;
import br.com.trackflow.order.service.customer.entity.Customer;
import br.com.trackflow.order.service.order.enums.OrderStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;

@Entity
@Table(name = "orders", uniqueConstraints = {
        @UniqueConstraint(name = "uk_orders_tracking_code", columnNames = "tracking_code")
}, indexes = {
        @Index(name = "idx_orders_customer", columnList = "customer_id"),
        @Index(name = "idx_orders_created_by", columnList = "created_by"),
        @Index(name = "idx_orders_status", columnList = "status")
})
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tracking_code", nullable = false, length = 40, updatable = false)
    private String trackingCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "origin_address_id", nullable = false)
    private Address originAddress;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "destination_address_id", nullable = false)
    private Address destinationAddress;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderStatus status;

    @Column(length = 500)
    private String description;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("occurredAt ASC")
    private List<OrderStatusHistory> statusHistory = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    private Long version;

    protected Order() {
    }

    public Order(
            String trackingCode,
            Customer customer,
            Address originAddress,
            Address destinationAddress,
            UUID createdBy,
            String description) {

        this.trackingCode = trackingCode;
        this.customer = customer;
        this.originAddress = originAddress;
        this.destinationAddress = destinationAddress;
        this.createdBy = createdBy;
        this.description = description;
        this.status = OrderStatus.CREATED;

        addStatusHistory(
                OrderStatus.CREATED,
                "Pedido criado.",
                null);
    }

    private void addStatusHistory(
            OrderStatus status,
            String description,
            String location) {

        OrderStatusHistory history = new OrderStatusHistory(
                this,
                status,
                description,
                location);

        this.statusHistory.add(history);
    }

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Address getOriginAddress() {
        return originAddress;
    }

    public Address getDestinationAddress() {
        return destinationAddress;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public List<OrderStatusHistory> getStatusHistory() {
        return statusHistory;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Long getVersion() {
        return version;
    }
}
