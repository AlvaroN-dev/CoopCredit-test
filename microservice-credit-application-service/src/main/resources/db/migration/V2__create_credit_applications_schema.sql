-- V2__create_relationships.sql
-- Add all foreign key constraints and relationships

-- =====================================================
-- USER-ROLE RELATIONSHIPS
-- =====================================================

ALTER TABLE user_roles
    ADD CONSTRAINT fk_user_roles_user 
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE user_roles
    ADD CONSTRAINT fk_user_roles_role 
        FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE;

-- =====================================================
-- BUSINESS DOMAIN RELATIONSHIPS
-- =====================================================

-- Affiliate 1-N Credit Applications
ALTER TABLE credit_applications
    ADD CONSTRAINT fk_credit_application_affiliate 
        FOREIGN KEY (affiliate_id) REFERENCES affiliates(id) ON DELETE RESTRICT;

-- Credit Application 1-1 Risk Evaluation
ALTER TABLE risk_evaluations
    ADD CONSTRAINT fk_risk_evaluation_credit_application 
        FOREIGN KEY (credit_application_id) REFERENCES credit_applications(id) ON DELETE CASCADE;

-- =====================================================
-- COMMENTS
-- =====================================================

COMMENT ON CONSTRAINT fk_credit_application_affiliate ON credit_applications 
    IS 'One affiliate can have many credit applications';

COMMENT ON CONSTRAINT fk_risk_evaluation_credit_application ON risk_evaluations 
    IS 'One-to-one relationship: each credit application has one risk evaluation';
