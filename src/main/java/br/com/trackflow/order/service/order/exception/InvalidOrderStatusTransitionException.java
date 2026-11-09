package br.com.trackflow.order.service.order.exception;

import br.com.trackflow.order.service.order.enums.OrderStatus;

public class InvalidOrderStatusTransitionException extends RuntimeException {

    public InvalidOrderStatusTransitionException(
            OrderStatus currentStatus,
            OrderStatus nextStatus) {

        super("Transição de status inválida: "
                + currentStatus + " -> " + nextStatus);
    }
}