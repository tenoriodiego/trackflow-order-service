package br.com.trackflow.order.service.customer.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.trackflow.order.service.customer.entity.Customer;

public interface CustomerRepository
        extends JpaRepository<Customer, UUID> {

    Optional<Customer> findByDocument(String document);

    boolean existsByDocument(String document);
}
