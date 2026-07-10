/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_07_certificates/04_07_04_certificate_downloads.sql
Version : 1.0
==============================================================

Description:
Creates the certificate_downloads table to audit when and 
by whom a certificate was downloaded.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_07_certificates/04_07_01_certificates.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS lms;

SET search_path TO lms, public;

------------------------------------------------------------
-- CERTIFICATE DOWNLOADS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS lms.certificate_downloads
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    certificate_id      UUID NOT NULL,
    user_id             UUID,

    downloaded_at       TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_address          VARCHAR(45),

    --------------------------------------------------------
    --------------------------------------------------------
    -- Version
    --------------------------------------------------------
    version             INTEGER NOT NULL DEFAULT 0,

    -- Audit Fields (Append-only log)
    --------------------------------------------------------
    created_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          UUID,

    --------------------------------------------------------
    -- Constraints
    --------------------------------------------------------
    CONSTRAINT fk_cert_downloads_cert
        FOREIGN KEY(certificate_id)
        REFERENCES lms.certificates(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_cert_downloads_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_cert_downloads_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE lms.certificate_downloads IS
'Audit log tracking every time a certificate PDF is downloaded.';

COMMENT ON COLUMN lms.certificate_downloads.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN lms.certificate_downloads.certificate_id IS
'Reference to the downloaded certificate.';

COMMENT ON COLUMN lms.certificate_downloads.user_id IS
'User who triggered the download (could be NULL if publicly downloaded via verification page).';

COMMENT ON COLUMN lms.certificate_downloads.ip_address IS
'IP address from which the download was requested.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
