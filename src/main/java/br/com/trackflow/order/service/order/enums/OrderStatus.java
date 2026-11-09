package br.com.trackflow.order.service.order.enums;

public enum OrderStatus {

    CREATED,
    COLLECTED,
    IN_TRANSIT,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELLED;

    public boolean canTransitionTo(OrderStatus nextStatus) {

        if (nextStatus == null) {
            return false;
        }

        return switch (this) {
            case CREATED ->
                nextStatus == COLLECTED ||
                        nextStatus == CANCELLED;

            case COLLECTED ->
                nextStatus == IN_TRANSIT ||
                        nextStatus == CANCELLED;

            case IN_TRANSIT ->
                nextStatus == OUT_FOR_DELIVERY;

            case OUT_FOR_DELIVERY ->
                nextStatus == DELIVERED;

            case DELIVERED, CANCELLED -> false;
        };
    }
}