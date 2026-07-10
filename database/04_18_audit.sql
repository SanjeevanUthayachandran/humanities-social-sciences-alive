/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_18_audit.sql
Version : 1.0
==============================================================
Description:
System Audit Log Module
==============================================================
*/

\connect hssa_db;

SET search_path TO audit, public;

------------------------------------------------------------
-- AUDIT LOGS
------------------------------------------------------------

CREATE TABLE audit.audit_logs
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id UUID,

    action audit.audit_action
            NOT NULL,

    module_name VARCHAR(100)
            NOT NULL,

    table_name VARCHAR(100),

    record_id UUID,

    description TEXT,

    old_data JSONB,

    new_data JSONB,

    ip_address INET,

    user_agent TEXT,

    request_method VARCHAR(10),

    request_uri TEXT,

    http_status INTEGER,

    execution_time_ms INTEGER,

    session_id UUID,

    created_at TIMESTAMPTZ
            NOT NULL
            DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_audit_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- AUDIT SETTINGS
------------------------------------------------------------

CREATE TABLE audit.audit_settings
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    module_name VARCHAR(100)
            UNIQUE NOT NULL,

    audit_enabled BOOLEAN
            NOT NULL
            DEFAULT TRUE,

    store_old_values BOOLEAN
            NOT NULL
            DEFAULT TRUE,

    store_new_values BOOLEAN
            NOT NULL
            DEFAULT TRUE,

    retention_days INTEGER
            DEFAULT 365,

    created_at TIMESTAMPTZ
            DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMPTZ
            DEFAULT CURRENT_TIMESTAMP
);

------------------------------------------------------------
-- AUDIT LOGIN EVENTS
------------------------------------------------------------

CREATE TABLE audit.audit_login_events
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id UUID,

    email CITEXT,

    login_success BOOLEAN,

    ip_address INET,

    user_agent TEXT,

    created_at TIMESTAMPTZ
            DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_audit_login_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- INDEXES
------------------------------------------------------------

CREATE INDEX idx_audit_user
ON audit.audit_logs(user_id);

CREATE INDEX idx_audit_action
ON audit.audit_logs(action);

CREATE INDEX idx_audit_module
ON audit.audit_logs(module_name);

CREATE INDEX idx_audit_table
ON audit.audit_logs(table_name);

CREATE INDEX idx_audit_record
ON audit.audit_logs(record_id);

CREATE INDEX idx_audit_created
ON audit.audit_logs(created_at);

CREATE INDEX idx_audit_login_user
ON audit.audit_login_events(user_id);

CREATE INDEX idx_audit_login_created
ON audit.audit_login_events(created_at);

------------------------------------------------------------
-- DEFAULT SETTINGS
------------------------------------------------------------

INSERT INTO audit.audit_settings
(module_name)

VALUES

('AUTH'),

('USERS'),

('COURSES'),

('LESSONS'),

('ENROLLMENT'),

('PAYMENT'),

('CERTIFICATES'),

('RESEARCH'),

('PUBLICATIONS'),

('WORKSHOPS'),

('EVENTS'),

('READING_CLUB'),

('LITERARY'),

('NEWS'),

('NOTIFICATIONS'),

('CONTACT'),

('MEDIA');

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE audit.audit_logs IS
'Stores all user and system audit events.';

COMMENT ON TABLE audit.audit_settings IS
'Defines audit configuration for each module.';

COMMENT ON TABLE audit.audit_login_events IS
'Stores authentication login events for security analysis.';

------------------------------------------------------------
-- VERIFY
------------------------------------------------------------

SELECT
table_name
FROM information_schema.tables
WHERE table_schema='audit'
ORDER BY table_name;

------------------------------------------------------------
-- END OF FILE