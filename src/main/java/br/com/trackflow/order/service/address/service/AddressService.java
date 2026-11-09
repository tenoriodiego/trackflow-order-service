package br.com.trackflow.order.service.address.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.trackflow.order.service.address.entity.Address;
import br.com.trackflow.order.service.address.repository.AddressRepository;
import br.com.trackflow.order.service.shared.exception.ResourceNotFoundException;

@Service
@Transactional(readOnly = true)
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Transactional
    public Address createAddress(
            String street,
            String number,
            String complement,
            String neighborhood,
            String city,
            String state,
            String postalCode,
            String country) {

        Address address = new Address(
                street,
                number,
                complement,
                neighborhood,
                city,
                state,
                postalCode,
                country);

        return addressRepository.save(address);
    }

    public Address findById(UUID id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Endereço não encontrado. ID: " + id));
    }
}