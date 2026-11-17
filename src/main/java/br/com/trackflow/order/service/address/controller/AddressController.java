package br.com.trackflow.order.service.address.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.trackflow.order.service.address.dto.request.CreateAddressRequest;
import br.com.trackflow.order.service.address.dto.response.AddressResponse;
import br.com.trackflow.order.service.address.entity.Address;
import br.com.trackflow.order.service.address.service.AddressService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<AddressResponse> createAddress(
            @Valid @RequestBody CreateAddressRequest request) {

        Address address = addressService.createAddress(
                request.street(),
                request.number(),
                request.complement(),
                request.neighborhood(),
                request.city(),
                request.state(),
                request.postalCode(),
                request.country());

        AddressResponse response = AddressResponse.from(address);

        return ResponseEntity
                .created(URI.create("/api/addresses/" + address.getId()))
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressResponse> findById(
            @PathVariable UUID id) {

        Address address = addressService.findById(id);

        return ResponseEntity.ok(AddressResponse.from(address));
    }
}