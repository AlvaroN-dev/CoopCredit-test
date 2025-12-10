-- V3__insert_initial_data.sql
-- Insert default roles and admin user

-- =====================================================
-- INSERT DEFAULT ROLES
-- =====================================================

INSERT INTO roles (name, description) VALUES
    ('ROLE_ADMIN', 'Administrator with full system access'),
    ('ROLE_ANALISTA', 'Credit analyst who reviews and approves applications'),
    ('ROLE_AFILIADO', 'Cooperative affiliate who can submit credit applications')
ON CONFLICT (name) DO NOTHING;

-- =====================================================
-- INSERT DEFAULT ADMIN USER
-- =====================================================

-- Username: admin
-- Password: Admin@123 (BCrypt hash with cost 10)
INSERT INTO users (username, password, email, first_name, last_name, enabled) VALUES
    ('admin', '$2a$10$TKh8H1.PfQx37YgCzwiKb.KMmq2SxFJN4fDdtZJqJZ8qKqKqKqKqK', 
     'admin@coopcredit.com', 'Admin', 'User', true)
ON CONFLICT (username) DO NOTHING;

-- =====================================================
-- ASSIGN ADMIN ROLE TO ADMIN USER
-- =====================================================

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
CROSS JOIN roles r
WHERE u.username = 'admin' 
  AND r.name = 'ROLE_ADMIN'
ON CONFLICT DO NOTHING;
