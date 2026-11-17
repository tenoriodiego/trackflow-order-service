package br.com.trackflow.order.service.customer.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateCustomerRequest(

        @NotBlank(message = "O nome do cliente é obrigatório.") String name,

        @NotBlank(message = "O documento do cliente é obrigatório.") String document,

        @NotBlank(message = "O e-mail do cliente é obrigatório.") @Email(message = "Informe um endereço de e-mail válido.") String email,

        @NotBlank(message = "O telefone do cliente é obrigatório.") String phone

) {
}