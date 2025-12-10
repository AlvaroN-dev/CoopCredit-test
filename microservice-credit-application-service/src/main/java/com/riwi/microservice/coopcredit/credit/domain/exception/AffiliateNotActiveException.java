package com.riwi.microservice.coopcredit.credit.domain.exception;

/**
 * Exception thrown when an affiliate is not active and cannot perform operations.
 */
public class AffiliateNotActiveException extends DomainException {
    
    private static final String CODE = "AFFILIATE_NOT_ACTIVE";
    
    public AffiliateNotActiveException(Long affiliateId) {
        super(CODE, "El afiliado con ID " + affiliateId + " no está activo. Solo los afiliados ACTIVOS pueden solicitar créditos.");
    }
}
