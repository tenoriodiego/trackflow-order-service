package br.com.trackflow.order.service.order.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.trackflow.order.service.address.entity.Address;
import br.com.trackflow.order.service.address.repository.AddressRepository;
import br.com.trackflow.order.service.customer.entity.Customer;
import br.com.trackflow.order.service.customer.repository.CustomerRepository;
import br.com.trackflow.order.service.order.entity.Order;
import br.com.trackflow.order.service.order.entity.OrderStatusHistory;
import br.com.trackflow.order.service.order.enums.OrderStatus;
import br.com.trackflow.order.service.order.exception.TrackingCodeGenerationException;
import br.com.trackflow.order.service.order.repository.OrderRepository;
import br.com.trackflow.order.service.order.repository.OrderStatusHistoryRepository;
import br.com.trackflow.order.service.shared.exception.ResourceNotFoundException;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private static final int MAX_TRACKING_CODE_ATTEMPTS = 5;

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final OrderRepository orderRepository;
    private final OrderStatusHistoryRepository orderStatusHistoryRepository;
    private final TrackingCodeGenerator trackingCodeGenerator;

    public OrderService(
            CustomerRepository customerRepository,
            AddressRepository addressRepository,
            OrderRepository orderRepository,
            OrderStatusHistoryRepository orderStatusHistoryRepository,
            TrackingCodeGenerator trackingCodeGenerator) {

        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.orderRepository = orderRepository;
        this.orderStatusHistoryRepository = orderStatusHistoryRepository;
        this.trackingCodeGenerator = trackingCodeGenerator;
    }

    @Transactional
    public Order createOrder(
            UUID customerId,
            UUID originAddressId,
            UUID destinationAddressId,
            UUID createdBy,
            String description) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente não encontrado. ID: " + customerId));

        Address originAddress = addressRepository.findById(originAddressId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Endereço de origem não encontrado. ID: " + originAddressId));

        Address destinationAddress = addressRepository.findById(destinationAddressId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Endereço de destino não encontrado. ID: " + destinationAddressId));

        String trackingCode = generateUniqueTrackingCode();

        Order order = new Order(
                trackingCode,
                customer,
                originAddress,
                destinationAddress,
                createdBy,
                description);

        return orderRepository.save(order);
    }

    public Order findById(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pedido não encontrado. ID: " + id));
    }

    public Order findByTrackingCode(String trackingCode) {
        return orderRepository.findByTrackingCode(trackingCode)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pedido não encontrado para o código de rastreamento informado."));
    }

    public Page<Order> findByCreatedBy(UUID createdBy, Pageable pageable) {
        return orderRepository.findByCreatedBy(createdBy, pageable);
    }

    @Transactional
    public Order changeStatus(
            UUID orderId,
            OrderStatus newStatus,
            String description,
            String location) {

        Order order = findById(orderId);

        order.changeStatus(
                newStatus,
                description,
                location);

        return orderRepository.save(order);
    }

    public Page<OrderStatusHistory> findStatusHistory(
            UUID orderId,
            Pageable pageable) {

        // Garante que o pedido existe antes de consultar seu histórico.
        findById(orderId);

        return orderStatusHistoryRepository
                .findByOrder_IdOrderByOccurredAtDesc(orderId, pageable);
    }

    private String generateUniqueTrackingCode() {

        for (int attempt = 0; attempt < MAX_TRACKING_CODE_ATTEMPTS; attempt++) {

            String trackingCode = trackingCodeGenerator.generate();

            if (!orderRepository.existsByTrackingCode(trackingCode)) {
                return trackingCode;
            }
        }

        throw new TrackingCodeGenerationException(
                "Não foi possível gerar um código de rastreamento único após "
                        + MAX_TRACKING_CODE_ATTEMPTS + " tentativas.");
    }
}