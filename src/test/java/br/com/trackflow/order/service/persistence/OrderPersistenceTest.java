package br.com.trackflow.order.service.persistence;

import br.com.trackflow.order.service.address.entity.Address;
import br.com.trackflow.order.service.address.repository.AddressRepository;
import br.com.trackflow.order.service.customer.entity.Customer;
import br.com.trackflow.order.service.customer.repository.CustomerRepository;
import br.com.trackflow.order.service.order.entity.Order;
import br.com.trackflow.order.service.order.enums.OrderStatus;
import br.com.trackflow.order.service.order.repository.OrderRepository;
import br.com.trackflow.order.service.order.repository.OrderStatusHistoryRepository;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import org.springframework.data.domain.PageRequest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderPersistenceTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderStatusHistoryRepository historyRepository;

    @Test
    void shouldSaveAndFindCustomerByDocument() {

        Customer customer = createCustomer();

        customerRepository.saveAndFlush(customer);

        var result = customerRepository
                .findByDocument("12345678900");

        assertThat(result).isPresent();
        assertThat(result.get().getName())
                .isEqualTo("TrackFlow Test");
    }

    @Test
    void shouldSaveOrderWithInitialStatusHistory() {

        Customer customer = customerRepository
                .saveAndFlush(createCustomer());

        Address origin = addressRepository
                .saveAndFlush(createAddress("São Paulo"));

        Address destination = addressRepository
                .saveAndFlush(createAddress("Guarulhos"));

        UUID createdBy = UUID.randomUUID();

        Order order = new Order(
                "TF-TEST-0001",
                customer,
                origin,
                destination,
                createdBy,
                "Pedido de teste");

        orderRepository.saveAndFlush(order);

        var result = orderRepository
                .findByTrackingCode("TF-TEST-0001");

        assertThat(result).isPresent();

        Order savedOrder = result.get();

        assertThat(savedOrder.getStatus())
                .isEqualTo(OrderStatus.CREATED);

        assertThat(savedOrder.getCreatedBy())
                .isEqualTo(createdBy);

        assertThat(savedOrder.getCustomer().getId())
                .isEqualTo(customer.getId());

        assertThat(savedOrder.getOriginAddress().getCity())
                .isEqualTo("São Paulo");

        assertThat(savedOrder.getDestinationAddress().getCity())
                .isEqualTo("Guarulhos");

        assertThat(savedOrder.getStatusHistory())
                .hasSize(1);

        assertThat(savedOrder.getStatusHistory().get(0).getStatus())
                .isEqualTo(OrderStatus.CREATED);

        assertThat(historyRepository.count())
                .isEqualTo(1);
    }

    @Test
    void shouldFindOrdersByCreatedByWithPagination() {

        Customer customer = customerRepository
                .saveAndFlush(createCustomer());

        Address origin = addressRepository
                .saveAndFlush(createAddress("São Paulo"));

        Address destination = addressRepository
                .saveAndFlush(createAddress("Guarulhos"));

        UUID createdBy = UUID.randomUUID();

        Order order = new Order(
                "TF-TEST-0002",
                customer,
                origin,
                destination,
                createdBy,
                "Pedido paginado");

        orderRepository.saveAndFlush(order);

        var result = orderRepository.findByCreatedBy(
                createdBy,
                PageRequest.of(0, 10));

        assertThat(result.getContent())
                .hasSize(1);

        assertThat(result.getTotalElements())
                .isEqualTo(1);
    }

    private Customer createCustomer() {

        return new Customer(
                "TrackFlow Test",
                "12345678900",
                "teste@trackflow.com",
                "11999999999");
    }

    private Address createAddress(String city) {

        return new Address(
                "Rua de Teste",
                "100",
                null,
                "Centro",
                city,
                "SP",
                "07000000",
                "BR");
    }
}
