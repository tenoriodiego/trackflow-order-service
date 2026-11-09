package br.com.trackflow.order.service.address.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.trackflow.order.service.address.entity.Address;
import br.com.trackflow.order.service.address.repository.AddressRepository;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressService addressService;

    @Test
    void shouldCreateAddressSuccessfully() {

        when(addressRepository.save(any(Address.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Address address = addressService.createAddress(
                "Rua de Teste",
                "100",
                "Apto 10",
                "Centro",
                "Guarulhos",
                "SP",
                "07000000",
                "Brasil");

        assertEquals("Rua de Teste", address.getStreet());
        assertEquals("100", address.getNumber());
        assertEquals("Guarulhos", address.getCity());
        assertEquals("SP", address.getState());

        verify(addressRepository).save(any(Address.class));
    }
}
