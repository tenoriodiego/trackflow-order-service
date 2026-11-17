package br.com.trackflow.order.service.order.dto.response;

import java.time.Instant;
import java.util.UUID;

import br.com.trackflow.order.service.order.entity.Order;
import br.com.trackflow.order.service.order.enums.OrderStatus;

public record OrderResponse(
        UUID id,
        String trackingCode,
        UUID customerId,
        UUID originAddressId,
        UUID destinationAddressId,
        UUID createdBy,
        OrderStatus status,
        String description,
        Instant createdAt,
        Instant updatedAt) {

    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTrackingCode(),
                order.getCustomer().getId(),
                order.getOriginAddress().getId(),
                order.getDestinationAddress().getId(),
                order.getCreatedBy(),
                order.getStatus(),
                order.getDescription(),
                order.getCreatedAt(),
                order.getUpdatedAt());
    }
}