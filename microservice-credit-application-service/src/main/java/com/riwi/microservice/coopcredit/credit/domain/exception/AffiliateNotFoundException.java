package com.riwi.microservice.coopcredit.credit.domain.exception;

/**
 * Exception thrown when an affiliate is not found.
 */
public class AffiliateNotFoundException extends DomainException {
    
    private static final String CODE = "AFFILIATE_NOT_FOUND";
    
    public AffiliateNotFoundException(Long affiliateId) {
        super(CODE, "Afiliado no encontrado con ID: " + affiliateId);
    }
    
    public AffiliateNotFoundException(String document) {
        super(CODE, "Afiliado no encontrado con documento: " + document);
    }
}
