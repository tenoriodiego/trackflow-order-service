package br.com.trackflow.order.service.customer.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.trackflow.order.service.customer.dto.request.CreateCustomerRequest;
import br.com.trackflow.order.service.customer.dto.response.CustomerResponse;
import br.com.trackflow.order.service.customer.entity.Customer;
import br.com.trackflow.order.service.customer.service.CustomerService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(
            @Valid @RequestBody CreateCustomerRequest request) {

        Customer customer = customerService.createCustomer(
                request.name(),
                request.document(),
                request.email(),
                request.phone());

        CustomerResponse response = CustomerResponse.from(customer);

        return ResponseEntity
                .created(URI.create("/api/customers/" + customer.getId()))
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> findById(
            @PathVariable UUID id) {

        Customer customer = customerService.findById(id);

        return ResponseEntity.ok(CustomerResponse.from(customer));
    }
}
