package br.com.trackflow.order.service.customer.dto.response;

import java.time.Instant;
import java.util.UUID;

import br.com.trackflow.order.service.customer.entity.Customer;

public record CustomerResponse(
        UUID id,
        String name,
        String document,
        String email,
        String phone,
        Instant createdAt,
        Instant updatedAt) {

    public static CustomerResponse from(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getDocument(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getCreatedAt(),
                customer.getUpdatedAt());
    }
}