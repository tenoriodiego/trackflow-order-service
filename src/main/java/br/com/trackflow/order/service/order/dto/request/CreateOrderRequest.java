package br.com.trackflow.order.service.order.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateOrderRequest(

        @NotNull(message = "O cliente é obrigatório.") UUID customerId,

        @NotNull(message = "O endereço de origem é obrigatório.") UUID originAddressId,

        @NotNull(message = "O endereço de destino é obrigatório.") UUID destinationAddressId,

        @NotBlank(message = "A descrição do pedido é obrigatória.") String description

) {
}