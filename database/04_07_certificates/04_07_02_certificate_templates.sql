/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_07_certificates/04_07_02_certificate_templates.sql
Version : 1.0
==============================================================

Description:
Creates the certificate_templates table for managing dynamic 
certificate designs.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_17_media.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS lms;

SET search_path TO lms, public;

------------------------------------------------------------
-- CERTIFICATE TEMPLATES
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS lms.certificate_templates
(
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    name                    VARCHAR(100) NOT NULL,

    background_media_id     UUID,

    layout_json             JSONB NOT NULL,

    is_active               BOOLEAN NOT NULL DEFAULT TRUE,

    --------------------------------------------------------
    --------------------------------------------------------
    -- Version
    --------------------------------------------------------
    version             INTEGER NOT NULL DEFAULT 0,

    -- Audit Fields
    --------------------------------------------------------
    created_at              TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at              TIMESTAMPTZ,
    created_by              UUID,
    updated_by              UUID,

    --------------------------------------------------------
    -- Constraints
    --------------------------------------------------------
    CONSTRAINT fk_cert_templates_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_cert_templates_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE lms.certificate_templates IS
'Stores templates and layouts used to dynamically generate certificate PDFs.';

COMMENT ON COLUMN lms.certificate_templates.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN lms.certificate_templates.background_media_id IS
'Reference to the background image/PDF template in the media library.';

COMMENT ON COLUMN lms.certificate_templates.layout_json IS
'JSON object defining absolute positioning for text (name, course, date, etc.).';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
