package br.com.trackflow.order.service.customer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.trackflow.order.service.customer.entity.Customer;
import br.com.trackflow.order.service.customer.repository.CustomerRepository;
import br.com.trackflow.order.service.shared.exception.DuplicateResourceException;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void shouldCreateCustomerWhenDocumentDoesNotExist() {

        when(customerRepository.existsByDocument("12345678900"))
                .thenReturn(false);

        when(customerRepository.save(any(Customer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Customer customer = customerService.createCustomer(
                "Cliente Teste",
                "12345678900",
                "cliente@email.com",
                "11999999999");

        assertEquals("Cliente Teste", customer.getName());
        assertEquals("12345678900", customer.getDocument());
        assertEquals("cliente@email.com", customer.getEmail());

        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void shouldThrowExceptionWhenDocumentAlreadyExists() {

        when(customerRepository.existsByDocument("12345678900"))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> customerService.createCustomer(
                        "Cliente Teste",
                        "12345678900",
                        "cliente@email.com",
                        "11999999999"));
    }
}