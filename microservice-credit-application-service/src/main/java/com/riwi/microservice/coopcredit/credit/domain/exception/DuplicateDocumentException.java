package com.riwi.microservice.coopcredit.credit.domain.exception;

/**
 * Exception thrown when trying to create an affiliate with a duplicate document.
 */
public class DuplicateDocumentException extends DomainException {
    
    private static final String CODE = "DUPLICATE_DOCUMENT";
    
    public DuplicateDocumentException(String document) {
        super(CODE, "Ya existe un afiliado con el documento: " + document);
    }
}
