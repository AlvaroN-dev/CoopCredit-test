package com.riwi.microservice.coopcredit.credit.application.dto.affiliate;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Request DTO for updating an Affiliate.
 */

@Getter
@Setter
public class UpdateAffiliateRequest {

    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String firstName;

    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    private String lastName;

    @Email(message = "El email debe tener un formato válido")
    private String email;

    @Pattern(regexp = "^[0-9+\\-\\s()]{7,20}$", message = "El teléfono debe tener un formato válido")
    private String phone;

    @Size(max = 255, message = "La dirección no puede exceder 255 caracteres")
    private String address;

    @Positive(message = "El salario debe ser mayor a cero")
    @DecimalMin(value = "0.01", message = "El salario debe ser mayor a cero")
    private BigDecimal salary;

}
