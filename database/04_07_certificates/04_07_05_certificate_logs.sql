/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_07_certificates/04_07_05_certificate_logs.sql
Version : 1.0
==============================================================

Description:
Creates the certificate_logs table for strictly auditing all 
actions related to certificates (generation, revoking, etc.).

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_07_certificates/04_07_01_certificates.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS lms;

SET search_path TO lms, public;

------------------------------------------------------------
-- CERTIFICATE LOGS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS lms.certificate_logs
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    certificate_id      UUID NOT NULL,

    action_type         VARCHAR(100) NOT NULL,
    log_data            JSONB,

    --------------------------------------------------------
    -- Audit Fields (Append-only log)
    --------------------------------------------------------
    created_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          UUID,

    --------------------------------------------------------
    -- Constraints
    --------------------------------------------------------
    CONSTRAINT fk_certificate_logs_cert
        FOREIGN KEY(certificate_id)
        REFERENCES lms.certificates(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_certificate_logs_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE lms.certificate_logs IS
'Append-only log for tracing all state changes to certificates (e.g., ISSUED, REVOKED).';

COMMENT ON COLUMN lms.certificate_logs.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN lms.certificate_logs.certificate_id IS
'Reference to the certificate being logged.';

COMMENT ON COLUMN lms.certificate_logs.action_type IS
'The event action (e.g., GENERATED, EMAILED, REVOKED).';

COMMENT ON COLUMN lms.certificate_logs.log_data IS
'Any associated JSON payload or context for the log entry.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
