-- V1__create_schema.sql
-- Create all database tables for the credit application system

-- =====================================================
-- USERS AND ROLES TABLES
-- =====================================================

-- Roles table
CREATE TABLE IF NOT EXISTS roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    enabled BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- User-Roles junction table (Many-to-Many)
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id)
);

-- =====================================================
-- BUSINESS DOMAIN TABLES
-- =====================================================

-- Affiliates table
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
    employment_start_date DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT chk_affiliate_status CHECK (status IN ('ACTIVO', 'INACTIVO', 'SUSPENDIDO', 'RETIRADO')),
    CONSTRAINT chk_salary_positive CHECK (salary > 0)
);

-- Credit Applications table
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
    
    CONSTRAINT chk_application_status 
        CHECK (status IN ('PENDIENTE', 'EN_REVISION', 'APROBADA', 'RECHAZADA', 'CANCELADA')),
    CONSTRAINT chk_requested_amount_positive CHECK (requested_amount > 0),
    CONSTRAINT chk_term_months_range CHECK (term_months >= 6 AND term_months <= 84),
    CONSTRAINT chk_interest_rate_positive CHECK (interest_rate > 0)
);

-- Risk Evaluations table
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
    
    CONSTRAINT chk_risk_level 
        CHECK (risk_level IN ('BAJO', 'MEDIO', 'ALTO', 'MUY_ALTO')),
    CONSTRAINT chk_credit_score_range CHECK (credit_score >= 300 AND credit_score <= 850),
    CONSTRAINT chk_dti_range CHECK (debt_to_income_ratio >= 0 AND debt_to_income_ratio <= 100),
    CONSTRAINT chk_years_employed_positive CHECK (years_employed >= 0)
);

-- =====================================================
-- INDEXES FOR PERFORMANCE
-- =====================================================

-- Users indexes
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_enabled ON users(enabled);

-- Roles indexes
CREATE INDEX idx_roles_name ON roles(name);

-- Affiliates indexes
CREATE INDEX idx_affiliate_document ON affiliates(document);
CREATE INDEX idx_affiliate_email ON affiliates(email);
CREATE INDEX idx_affiliate_status ON affiliates(status);

-- Credit Applications indexes
CREATE INDEX idx_credit_application_number ON credit_applications(application_number);
CREATE INDEX idx_credit_application_status ON credit_applications(status);
CREATE INDEX idx_credit_application_affiliate ON credit_applications(affiliate_id);
CREATE INDEX idx_credit_application_date ON credit_applications(application_date);

-- Risk Evaluations indexes
CREATE INDEX idx_risk_evaluation_credit_app ON risk_evaluations(credit_application_id);
CREATE INDEX idx_risk_evaluation_risk_level ON risk_evaluations(risk_level);
CREATE INDEX idx_risk_evaluation_evaluated_by ON risk_evaluations(evaluated_by);

-- =====================================================
-- TABLE COMMENTS
-- =====================================================

COMMENT ON TABLE users IS 'System users with authentication credentials';
COMMENT ON TABLE roles IS 'User roles for authorization (ADMIN, ANALISTA, AFILIADO)';
COMMENT ON TABLE user_roles IS 'Many-to-Many relationship between users and roles';

COMMENT ON TABLE affiliates IS 'Cooperative affiliates who can apply for credits';
COMMENT ON TABLE credit_applications IS 'Credit application requests submitted by affiliates';
COMMENT ON TABLE risk_evaluations IS 'Risk assessment results for credit applications';
