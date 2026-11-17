package br.com.trackflow.order.service.order.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.trackflow.order.service.order.dto.request.ChangeOrderStatusRequest;
import br.com.trackflow.order.service.order.dto.request.CreateOrderRequest;
import br.com.trackflow.order.service.order.dto.response.OrderResponse;
import br.com.trackflow.order.service.order.dto.response.OrderStatusHistoryResponse;
import br.com.trackflow.order.service.order.entity.Order;
import br.com.trackflow.order.service.order.entity.OrderStatusHistory;
import br.com.trackflow.order.service.order.service.OrderService;
import br.com.trackflow.order.service.shared.dto.response.PageResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            @RequestHeader("X-User-Id") UUID createdBy) {

        Order order = orderService.createOrder(
                request.customerId(),
                request.originAddressId(),
                request.destinationAddressId(),
                createdBy,
                request.description());

        OrderResponse response = OrderResponse.from(order);

        return ResponseEntity
                .created(URI.create("/api/orders/" + order.getId()))
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(
            @PathVariable UUID id) {

        Order order = orderService.findById(id);

        return ResponseEntity.ok(OrderResponse.from(order));
    }

    @GetMapping("/tracking/{trackingCode}")
    public ResponseEntity<OrderResponse> findByTrackingCode(
            @PathVariable String trackingCode) {

        Order order = orderService.findByTrackingCode(trackingCode);

        return ResponseEntity.ok(OrderResponse.from(order));
    }

    @GetMapping
    public ResponseEntity<PageResponse<OrderResponse>> findByCreatedBy(
            @RequestHeader("X-User-Id") UUID createdBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = createPageable(page, size);

        Page<Order> orders = orderService.findByCreatedBy(
                createdBy,
                pageable);

        PageResponse<OrderResponse> response = PageResponse.from(
                orders,
                OrderResponse::from);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> changeStatus(
            @PathVariable UUID id,
            @Valid @RequestBody ChangeOrderStatusRequest request) {

        Order order = orderService.changeStatus(
                id,
                request.status(),
                request.description(),
                request.location());

        return ResponseEntity.ok(OrderResponse.from(order));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<PageResponse<OrderStatusHistoryResponse>> findStatusHistory(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = createPageable(page, size);

        Page<OrderStatusHistory> history = orderService.findStatusHistory(
                id,
                pageable);

        PageResponse<OrderStatusHistoryResponse> response = PageResponse.from(
                history,
                OrderStatusHistoryResponse::from);

        return ResponseEntity.ok(response);
    }

    private Pageable createPageable(int page, int size) {

        if (page < 0) {
            throw new IllegalArgumentException(
                    "O número da página não pode ser negativo.");
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "O tamanho da página deve estar entre 1 e 100.");
        }

        return PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt"));
    }
}