package br.com.trackflow.order.service.address.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateAddressRequest(

        @NotBlank(message = "A rua é obrigatória.") String street,

        @NotBlank(message = "O número é obrigatório.") String number,

        String complement,

        @NotBlank(message = "O bairro é obrigatório.") String neighborhood,

        @NotBlank(message = "A cidade é obrigatória.") String city,

        @NotBlank(message = "O estado é obrigatório.") String state,

        @NotBlank(message = "O CEP é obrigatório.") String postalCode,

        @NotBlank(message = "O país é obrigatório.") String country

) {
}