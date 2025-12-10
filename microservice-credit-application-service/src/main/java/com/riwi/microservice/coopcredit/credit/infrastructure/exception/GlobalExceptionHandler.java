package com.riwi.microservice.coopcredit.credit.infrastructure.exception;

import com.riwi.microservice.coopcredit.credit.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler using RFC 7807 ProblemDetail format.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String BASE_URI = "https://api.coopcredit.com/errors/";

    /**
     * Handle AffiliateNotFoundException.
     */
    @ExceptionHandler(AffiliateNotFoundException.class)
    public ProblemDetail handleAffiliateNotFoundException(AffiliateNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setType(URI.create(BASE_URI + "affiliate-not-found"));
        problemDetail.setTitle("Afiliado no encontrado");
        problemDetail.setProperty("errorCode", ex.getCode());
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    /**
     * Handle DuplicateDocumentException.
     */
    @ExceptionHandler(DuplicateDocumentException.class)
    public ProblemDetail handleDuplicateDocumentException(DuplicateDocumentException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setType(URI.create(BASE_URI + "duplicate-document"));
        problemDetail.setTitle("Documento duplicado");
        problemDetail.setProperty("errorCode", ex.getCode());
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    /**
     * Handle AffiliateNotActiveException.
     */
    @ExceptionHandler(AffiliateNotActiveException.class)
    public ProblemDetail handleAffiliateNotActiveException(AffiliateNotActiveException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        problemDetail.setType(URI.create(BASE_URI + "affiliate-not-active"));
        problemDetail.setTitle("Afiliado no activo");
        problemDetail.setProperty("errorCode", ex.getCode());
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    /**
     * Handle CreditApplicationNotFoundException.
     */
    @ExceptionHandler(CreditApplicationNotFoundException.class)
    public ProblemDetail handleCreditApplicationNotFoundException(CreditApplicationNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setType(URI.create(BASE_URI + "credit-application-not-found"));
        problemDetail.setTitle("Solicitud de crédito no encontrada");
        problemDetail.setProperty("errorCode", ex.getCode());
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    /**
     * Handle CreditAmountExceededException.
     */
    @ExceptionHandler(CreditAmountExceededException.class)
    public ProblemDetail handleCreditAmountExceededException(CreditAmountExceededException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        problemDetail.setType(URI.create(BASE_URI + "credit-amount-exceeded"));
        problemDetail.setTitle("Monto de crédito excedido");
        problemDetail.setProperty("errorCode", ex.getCode());
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    /**
     * Handle InvalidCreditStatusException.
     */
    @ExceptionHandler(InvalidCreditStatusException.class)
    public ProblemDetail handleInvalidCreditStatusException(InvalidCreditStatusException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        problemDetail.setType(URI.create(BASE_URI + "invalid-credit-status"));
        problemDetail.setTitle("Estado de crédito inválido");
        problemDetail.setProperty("errorCode", ex.getCode());
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    /**
     * Handle RiskEvaluationNotFoundException.
     */
    @ExceptionHandler(RiskEvaluationNotFoundException.class)
    public ProblemDetail handleRiskEvaluationNotFoundException(RiskEvaluationNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setType(URI.create(BASE_URI + "risk-evaluation-not-found"));
        problemDetail.setTitle("Evaluación de riesgo no encontrada");
        problemDetail.setProperty("errorCode", ex.getCode());
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    /**
     * Handle RiskEvaluationAlreadyExistsException.
     */
    @ExceptionHandler(RiskEvaluationAlreadyExistsException.class)
    public ProblemDetail handleRiskEvaluationAlreadyExistsException(RiskEvaluationAlreadyExistsException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setType(URI.create(BASE_URI + "risk-evaluation-already-exists"));
        problemDetail.setTitle("Evaluación de riesgo ya existe");
        problemDetail.setProperty("errorCode", ex.getCode());
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    /**
     * Handle InvalidSalaryException.
     */
    @ExceptionHandler(InvalidSalaryException.class)
    public ProblemDetail handleInvalidSalaryException(InvalidSalaryException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setType(URI.create(BASE_URI + "invalid-salary"));
        problemDetail.setTitle("Salario inválido");
        problemDetail.setProperty("errorCode", ex.getCode());
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    /**
     * Handle validation errors.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Error de validación en los datos de entrada");
        problemDetail.setType(URI.create(BASE_URI + "validation-error"));
        problemDetail.setTitle("Error de validación");
        problemDetail.setProperty("errorCode", "VALIDATION_ERROR");
        problemDetail.setProperty("errors", errors);
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    /**
     * Handle generic domain exceptions.
     */
    @ExceptionHandler(DomainException.class)
    public ProblemDetail handleDomainException(DomainException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setType(URI.create(BASE_URI + "domain-error"));
        problemDetail.setTitle("Error de dominio");
        problemDetail.setProperty("errorCode", ex.getCode());
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    /**
     * Handle AccessDeniedException.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDeniedException(AccessDeniedException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN, "Acceso denegado: No tiene permisos para realizar esta acción");
        problemDetail.setType(URI.create(BASE_URI + "access-denied"));
        problemDetail.setTitle("Acceso denegado");
        problemDetail.setProperty("errorCode", "ACCESS_DENIED");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    /**
     * Handle all other exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, "Ha ocurrido un error interno en el servidor");
        problemDetail.setType(URI.create(BASE_URI + "internal-server-error"));
        problemDetail.setTitle("Error interno del servidor");
        problemDetail.setProperty("errorCode", "INTERNAL_ERROR");
        problemDetail.setProperty("timestamp", Instant.now());
        // Log the actual exception for debugging
        return problemDetail;
    }
}
