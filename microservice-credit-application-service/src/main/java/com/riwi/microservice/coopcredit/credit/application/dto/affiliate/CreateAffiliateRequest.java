package com.riwi.microservice.coopcredit.credit.application.dto.affiliate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request DTO for creating an Affiliate.
 */
@Schema(description = "Request DTO for creating an Affiliate")
public class CreateAffiliateRequest {

    @NotBlank(message = "El documento es obligatorio")
    @Size(min = 5, max = 20, message = "El documento debe tener entre 5 y 20 caracteres")
    @Schema(description = "Document number", example = "123456789")
    private String document;

    @NotBlank(message = "El tipo de documento es obligatorio")
    @Size(max = 20, message = "El tipo de documento no puede exceder 20 caracteres")
    @Schema(description = "Document type", example = "CC")
    private String documentType;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Schema(description = "First name", example = "John")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    @Schema(description = "Last name", example = "Doe")
    private String lastName;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    @Schema(description = "Email address", example = "john.doe@example.com")
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9+\\-\\s()]{7,20}$", message = "El teléfono debe tener un formato válido")
    @Schema(description = "Phone number", example = "+57 300 123 4567")
    private String phone;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    @Schema(description = "Birth date", example = "1990-01-01")
    private LocalDate birthDate;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 255, message = "La dirección no puede exceder 255 caracteres")
    @Schema(description = "Address", example = "Calle 123 # 45-67")
    private String address;

    @NotNull(message = "El salario es obligatorio")
    @Positive(message = "El salario debe ser mayor a cero")
    @DecimalMin(value = "0.01", message = "El salario debe ser mayor a cero")
    @Schema(description = "Monthly salary", example = "2500000")
    private BigDecimal salary;

    // Getters and Setters
    public String getDocument() { return document; }
    public void setDocument(String document) { this.document = document; }

    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }
}
