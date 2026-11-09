package br.com.trackflow.order.service.order.service;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.trackflow.order.service.address.entity.Address;
import br.com.trackflow.order.service.address.repository.AddressRepository;
import br.com.trackflow.order.service.customer.entity.Customer;
import br.com.trackflow.order.service.customer.repository.CustomerRepository;
import br.com.trackflow.order.service.order.entity.Order;
import br.com.trackflow.order.service.order.enums.OrderStatus;
import br.com.trackflow.order.service.order.exception.InvalidOrderStatusTransitionException;
import br.com.trackflow.order.service.order.exception.TrackingCodeGenerationException;
import br.com.trackflow.order.service.order.repository.OrderRepository;
import br.com.trackflow.order.service.order.repository.OrderStatusHistoryRepository;
import br.com.trackflow.order.service.shared.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderStatusHistoryRepository orderStatusHistoryRepository;

    @Mock
    private TrackingCodeGenerator trackingCodeGenerator;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldCreateOrderSuccessfully() {

        UUID customerId = UUID.randomUUID();
        UUID originAddressId = UUID.randomUUID();
        UUID destinationAddressId = UUID.randomUUID();
        UUID createdBy = UUID.randomUUID();

        Customer customer = new Customer(
                "Cliente Teste",
                "12345678900",
                "cliente@email.com",
                "11999999999");

        Address originAddress = new Address(
                "Rua de Origem",
                "100",
                null,
                "Centro",
                "Guarulhos",
                "SP",
                "07000000",
                "Brasil");

        Address destinationAddress = new Address(
                "Rua de Destino",
                "200",
                null,
                "Centro",
                "São Paulo",
                "SP",
                "01000000",
                "Brasil");

        String trackingCode = "TF-ABC123DEF456";

        when(customerRepository.findById(customerId))
                .thenReturn(Optional.of(customer));

        when(addressRepository.findById(originAddressId))
                .thenReturn(Optional.of(originAddress));

        when(addressRepository.findById(destinationAddressId))
                .thenReturn(Optional.of(destinationAddress));

        when(trackingCodeGenerator.generate())
                .thenReturn(trackingCode);

        when(orderRepository.existsByTrackingCode(trackingCode))
                .thenReturn(false);

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order order = orderService.createOrder(
                customerId,
                originAddressId,
                destinationAddressId,
                createdBy,
                "Pedido de teste");

        assertEquals(trackingCode, order.getTrackingCode());
        assertEquals(customer, order.getCustomer());
        assertEquals(createdBy, order.getCreatedBy());
        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertEquals(1, order.getStatusHistory().size());

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void shouldThrowExceptionWhenCustomerDoesNotExist() {

        UUID customerId = UUID.randomUUID();

        when(customerRepository.findById(customerId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> orderService.createOrder(
                        customerId,
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        "Pedido de teste"));

        verify(addressRepository, never()).findById(any());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void shouldThrowExceptionWhenTrackingCodeCannotBeGenerated() {

        UUID customerId = UUID.randomUUID();
        UUID originAddressId = UUID.randomUUID();
        UUID destinationAddressId = UUID.randomUUID();

        Customer customer = new Customer(
                "Cliente Teste",
                "12345678900",
                "cliente@email.com",
                "11999999999");

        Address originAddress = new Address(
                "Rua de Origem",
                "100",
                null,
                "Centro",
                "Guarulhos",
                "SP",
                "07000000",
                "Brasil");

        Address destinationAddress = new Address(
                "Rua de Destino",
                "200",
                null,
                "Centro",
                "São Paulo",
                "SP",
                "01000000",
                "Brasil");

        String duplicatedCode = "TF-ABC123DEF456";

        when(customerRepository.findById(customerId))
                .thenReturn(Optional.of(customer));

        when(addressRepository.findById(originAddressId))
                .thenReturn(Optional.of(originAddress));

        when(addressRepository.findById(destinationAddressId))
                .thenReturn(Optional.of(destinationAddress));

        when(trackingCodeGenerator.generate())
                .thenReturn(duplicatedCode);

        when(orderRepository.existsByTrackingCode(duplicatedCode))
                .thenReturn(true);

        assertThrows(
                TrackingCodeGenerationException.class,
                () -> orderService.createOrder(
                        customerId,
                        originAddressId,
                        destinationAddressId,
                        UUID.randomUUID(),
                        "Pedido de teste"));

        verify(trackingCodeGenerator, times(5)).generate();
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void shouldChangeOrderStatusAndRegisterHistory() {

        UUID orderId = UUID.randomUUID();

        Order order = createOrder();

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order updatedOrder = orderService.changeStatus(
                orderId,
                OrderStatus.COLLECTED,
                "Pedido coletado pela transportadora",
                "Guarulhos/SP");

        assertEquals(OrderStatus.COLLECTED, updatedOrder.getStatus());
        assertEquals(2, updatedOrder.getStatusHistory().size());

        verify(orderRepository).save(order);
    }

    @Test
    void shouldRejectInvalidStatusTransition() {

        UUID orderId = UUID.randomUUID();

        Order order = createOrder();

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        assertThrows(
                InvalidOrderStatusTransitionException.class,
                () -> orderService.changeStatus(
                        orderId,
                        OrderStatus.DELIVERED,
                        "Tentativa de entrega",
                        "São Paulo/SP"));

        verify(orderRepository, never()).save(any(Order.class));
    }

    private Order createOrder() {

        Customer customer = new Customer(
                "Cliente Teste",
                "12345678900",
                "cliente@email.com",
                "11999999999");

        Address originAddress = new Address(
                "Rua de Origem",
                "100",
                null,
                "Centro",
                "Guarulhos",
                "SP",
                "07000000",
                "Brasil");

        Address destinationAddress = new Address(
                "Rua de Destino",
                "200",
                null,
                "Centro",
                "São Paulo",
                "SP",
                "01000000",
                "Brasil");

        return new Order(
                "TF-ABC123DEF456",
                customer,
                originAddress,
                destinationAddress,
                UUID.randomUUID(),
                "Pedido de teste");
    }
}