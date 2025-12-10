package com.riwi.microservice.coopcredit.credit.application.dto.affiliate;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * Request DTO for updating an Affiliate.
 */
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

    // Getters and Setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }
}
