/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_07_certificates/04_07_03_certificate_verification.sql
Version : 1.0
==============================================================

Description:
Creates the certificate_verification table to store unique 
verification URLs/QR codes for authenticating certificates.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_07_certificates/04_07_01_certificates.sql
- 04_17_media.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS lms;

SET search_path TO lms, public;

------------------------------------------------------------
-- CERTIFICATE VERIFICATION
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS lms.certificate_verification
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    certificate_id      UUID NOT NULL,

    verification_code   VARCHAR(100) NOT NULL,
    qr_code_media_id    UUID,

    --------------------------------------------------------
    --------------------------------------------------------
    -- Version
    --------------------------------------------------------
    version             INTEGER NOT NULL DEFAULT 0,

    -- Audit Fields
    --------------------------------------------------------
    created_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at          TIMESTAMPTZ,
    created_by          UUID,
    updated_by          UUID,

    --------------------------------------------------------
    -- Constraints
    --------------------------------------------------------
    CONSTRAINT uq_cert_verification_code
        UNIQUE(verification_code),

    CONSTRAINT uq_cert_verification_cert
        UNIQUE(certificate_id),

    CONSTRAINT fk_cert_verification_cert
        FOREIGN KEY(certificate_id)
        REFERENCES lms.certificates(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_cert_verification_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_cert_verification_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE lms.certificate_verification IS
'Manages public verification data for issued certificates.';

COMMENT ON COLUMN lms.certificate_verification.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN lms.certificate_verification.verification_code IS
'Randomly generated code used in public verification URLs.';

COMMENT ON COLUMN lms.certificate_verification.qr_code_media_id IS
'Reference to the generated QR code image file.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
