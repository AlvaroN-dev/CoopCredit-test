-- V1__create_affiliates_schema.sql
-- Create affiliates table

CREATE TABLE IF NOT EXISTS affiliates (
    id BIGSERIAL PRIMARY KEY,
    document VARCHAR(20) NOT NULL UNIQUE,
    document_type VARCHAR(20) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    birth_date DATE NOT NULL,
    address VARCHAR(255) NOT NULL,
    salary DECIMAL(15, 2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT chk_affiliate_status CHECK (status IN ('ACTIVO', 'INACTIVO', 'SUSPENDIDO', 'RETIRADO')),
    CONSTRAINT chk_salary_positive CHECK (salary > 0)
);

-- Create indexes
CREATE INDEX idx_affiliate_document ON affiliates(document);
CREATE INDEX idx_affiliate_email ON affiliates(email);
CREATE INDEX idx_affiliate_status ON affiliates(status);

-- Add comments
COMMENT ON TABLE affiliates IS 'Table storing affiliate information';
COMMENT ON COLUMN affiliates.document IS 'Unique identification document number';
COMMENT ON COLUMN affiliates.document_type IS 'Type of document (CC, CE, NIT, etc.)';
COMMENT ON COLUMN affiliates.salary IS 'Monthly salary of the affiliate';
COMMENT ON COLUMN affiliates.status IS 'Current status of the affiliate';
