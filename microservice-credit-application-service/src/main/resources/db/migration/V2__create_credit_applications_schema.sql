-- V2__create_credit_applications_schema.sql
-- Create credit_applications table

CREATE TABLE IF NOT EXISTS credit_applications (
    id BIGSERIAL PRIMARY KEY,
    application_number VARCHAR(20) NOT NULL UNIQUE,
    requested_amount DECIMAL(15, 2) NOT NULL,
    term_months INTEGER NOT NULL,
    interest_rate DECIMAL(5, 2) NOT NULL,
    purpose VARCHAR(500) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
    comments VARCHAR(1000),
    application_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    review_date TIMESTAMP,
    decision_date TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    affiliate_id BIGINT NOT NULL,
    
    CONSTRAINT fk_credit_application_affiliate 
        FOREIGN KEY (affiliate_id) REFERENCES affiliates(id) ON DELETE RESTRICT,
    CONSTRAINT chk_application_status 
        CHECK (status IN ('PENDIENTE', 'EN_REVISION', 'APROBADA', 'RECHAZADA', 'CANCELADA')),
    CONSTRAINT chk_requested_amount_positive CHECK (requested_amount > 0),
    CONSTRAINT chk_term_months_range CHECK (term_months >= 6 AND term_months <= 84),
    CONSTRAINT chk_interest_rate_positive CHECK (interest_rate > 0)
);

-- Create indexes
CREATE INDEX idx_credit_application_number ON credit_applications(application_number);
CREATE INDEX idx_credit_application_status ON credit_applications(status);
CREATE INDEX idx_credit_application_affiliate ON credit_applications(affiliate_id);
CREATE INDEX idx_credit_application_date ON credit_applications(application_date);

-- Add comments
COMMENT ON TABLE credit_applications IS 'Table storing credit application requests';
COMMENT ON COLUMN credit_applications.application_number IS 'Unique application reference number';
COMMENT ON COLUMN credit_applications.requested_amount IS 'Amount requested for the credit';
COMMENT ON COLUMN credit_applications.term_months IS 'Loan term in months';
COMMENT ON COLUMN credit_applications.interest_rate IS 'Annual interest rate percentage';
COMMENT ON COLUMN credit_applications.status IS 'Current status of the application';
