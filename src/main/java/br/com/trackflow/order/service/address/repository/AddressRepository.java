package br.com.trackflow.order.service.address.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.trackflow.order.service.address.entity.Address;

public interface AddressRepository
        extends JpaRepository<Address, UUID> {
}
