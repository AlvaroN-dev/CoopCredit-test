-- V3__create_risk_evaluations_schema.sql
-- Create risk_evaluations table

CREATE TABLE IF NOT EXISTS risk_evaluations (
    id BIGSERIAL PRIMARY KEY,
    credit_score INTEGER NOT NULL,
    risk_level VARCHAR(20) NOT NULL,
    debt_to_income_ratio DECIMAL(5, 2) NOT NULL,
    has_default_history BOOLEAN NOT NULL DEFAULT FALSE,
    years_employed INTEGER NOT NULL,
    has_guarantor BOOLEAN NOT NULL DEFAULT FALSE,
    collateral_value DECIMAL(15, 2),
    evaluation_notes VARCHAR(2000),
    recommendation VARCHAR(500),
    approved BOOLEAN,
    evaluated_by VARCHAR(100) NOT NULL,
    evaluation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    credit_application_id BIGINT NOT NULL UNIQUE,
    
    CONSTRAINT fk_risk_evaluation_credit_application 
        FOREIGN KEY (credit_application_id) REFERENCES credit_applications(id) ON DELETE CASCADE,
    CONSTRAINT chk_risk_level 
        CHECK (risk_level IN ('BAJO', 'MEDIO', 'ALTO', 'MUY_ALTO')),
    CONSTRAINT chk_credit_score_range CHECK (credit_score >= 300 AND credit_score <= 850),
    CONSTRAINT chk_dti_range CHECK (debt_to_income_ratio >= 0 AND debt_to_income_ratio <= 100),
    CONSTRAINT chk_years_employed_positive CHECK (years_employed >= 0)
);

-- Create indexes
CREATE INDEX idx_risk_evaluation_credit_app ON risk_evaluations(credit_application_id);
CREATE INDEX idx_risk_evaluation_risk_level ON risk_evaluations(risk_level);
CREATE INDEX idx_risk_evaluation_evaluated_by ON risk_evaluations(evaluated_by);

-- Add comments
COMMENT ON TABLE risk_evaluations IS 'Table storing risk evaluation results for credit applications';
COMMENT ON COLUMN risk_evaluations.credit_score IS 'Credit score (300-850 range)';
COMMENT ON COLUMN risk_evaluations.risk_level IS 'Calculated risk level based on evaluation';
COMMENT ON COLUMN risk_evaluations.debt_to_income_ratio IS 'Debt-to-income ratio percentage';
COMMENT ON COLUMN risk_evaluations.has_default_history IS 'Whether applicant has history of defaults';
COMMENT ON COLUMN risk_evaluations.has_guarantor IS 'Whether application has a guarantor';
COMMENT ON COLUMN risk_evaluations.collateral_value IS 'Value of collateral if any';
