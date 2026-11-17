package br.com.trackflow.order.service.order.dto.request;

import br.com.trackflow.order.service.order.enums.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChangeOrderStatusRequest(

        @NotNull(message = "O novo status é obrigatório.") OrderStatus status,

        @NotBlank(message = "A descrição da movimentação é obrigatória.") String description,

        String location

) {
}