-- V4__insert_initial_data.sql
-- Insert sample affiliates data

INSERT INTO affiliates (document, document_type, first_name, last_name, email, phone, birth_date, address, salary, status)
VALUES 
    ('1234567890', 'CC', 'Juan', 'Pérez García', 'juan.perez@email.com', '+57 300 123 4567', '1985-03-15', 'Calle 100 #15-30, Bogotá', 5500000.00, 'ACTIVO'),
    ('0987654321', 'CC', 'María', 'González López', 'maria.gonzalez@email.com', '+57 310 987 6543', '1990-07-22', 'Carrera 45 #80-20, Medellín', 7200000.00, 'ACTIVO'),
    ('1122334455', 'CE', 'Carlos', 'Rodríguez Martín', 'carlos.rodriguez@email.com', '+57 320 112 2334', '1988-11-08', 'Avenida 68 #35-50, Cali', 4800000.00, 'ACTIVO'),
    ('5544332211', 'CC', 'Ana', 'Martínez Ruiz', 'ana.martinez@email.com', '+57 315 554 4332', '1992-05-30', 'Calle 72 #10-15, Barranquilla', 6000000.00, 'ACTIVO'),
    ('6677889900', 'CC', 'Pedro', 'Sánchez Torres', 'pedro.sanchez@email.com', '+57 305 667 7889', '1978-09-12', 'Carrera 30 #45-60, Cartagena', 8500000.00, 'ACTIVO'),
    ('1199228837', 'CC', 'Laura', 'Hernández Díaz', 'laura.hernandez@email.com', '+57 318 119 9228', '1995-01-25', 'Calle 50 #25-40, Bucaramanga', 3500000.00, 'INACTIVO');

-- Insert sample credit applications
INSERT INTO credit_applications (application_number, requested_amount, term_months, interest_rate, purpose, status, affiliate_id, application_date)
VALUES 
    ('CRE-202412-000001', 15000000.00, 36, 18.50, 'Compra de vehículo para uso personal y trabajo', 'APROBADA', 1, '2024-12-01 10:30:00'),
    ('CRE-202412-000002', 25000000.00, 48, 16.00, 'Remodelación de vivienda y mejoras del hogar', 'PENDIENTE', 2, '2024-12-05 14:15:00'),
    ('CRE-202412-000003', 8000000.00, 24, 19.00, 'Capital de trabajo para emprendimiento', 'EN_REVISION', 3, '2024-12-08 09:00:00'),
    ('CRE-202412-000004', 50000000.00, 60, 15.50, 'Consolidación de deudas existentes', 'RECHAZADA', 1, '2024-11-15 11:45:00'),
    ('CRE-202412-000005', 12000000.00, 36, 17.00, 'Educación superior para hijo', 'PENDIENTE', 4, '2024-12-10 16:30:00');

-- Insert sample risk evaluations for approved/rejected applications
INSERT INTO risk_evaluations (credit_score, risk_level, debt_to_income_ratio, has_default_history, years_employed, has_guarantor, collateral_value, evaluation_notes, recommendation, approved, evaluated_by, credit_application_id)
VALUES 
    (720, 'BAJO', 25.50, FALSE, 8, FALSE, NULL, 'Excelente historial crediticio. Ingresos estables. Sin antecedentes negativos.', 'APROBAR - Perfil de riesgo bajo.', TRUE, 'analyst_001', 1),
    (520, 'MUY_ALTO', 55.00, TRUE, 2, FALSE, NULL, 'Score crediticio bajo. Historial de incumplimiento previo. DTI elevado.', 'RECHAZAR - Perfil de riesgo muy alto.', FALSE, 'analyst_002', 4);

-- Add comments
COMMENT ON TABLE affiliates IS 'Initial test data for development';
