package br.com.trackflow.order.service.customer.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.trackflow.order.service.customer.entity.Customer;
import br.com.trackflow.order.service.customer.repository.CustomerRepository;
import br.com.trackflow.order.service.shared.exception.DuplicateResourceException;
import br.com.trackflow.order.service.shared.exception.ResourceNotFoundException;

@Service
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Customer createCustomer(
            String name,
            String document,
            String email,
            String phone) {

        String normalizedDocument = document.trim();

        if (customerRepository.existsByDocument(normalizedDocument)) {
            throw new DuplicateResourceException(
                    "Já existe um cliente cadastrado com o documento informado.");
        }

        Customer customer = new Customer(
                name,
                normalizedDocument,
                email,
                phone);

        return customerRepository.save(customer);
    }

    public Customer findById(UUID id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente não encontrado. ID: " + id));
    }

    public Customer findByDocument(String document) {
        return customerRepository.findByDocument(document.trim())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente não encontrado para o documento informado."));
    }
}