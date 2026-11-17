package br.com.trackflow.order.service.order.dto.response;

import java.time.Instant;
import java.util.UUID;

import br.com.trackflow.order.service.order.entity.OrderStatusHistory;
import br.com.trackflow.order.service.order.enums.OrderStatus;

public record OrderStatusHistoryResponse(
        UUID id,
        OrderStatus status,
        String description,
        String location,
        Instant occurredAt) {

    public static OrderStatusHistoryResponse from(OrderStatusHistory history) {
        return new OrderStatusHistoryResponse(
                history.getId(),
                history.getStatus(),
                history.getDescription(),
                history.getLocation(),
                history.getOccurredAt());
    }
}