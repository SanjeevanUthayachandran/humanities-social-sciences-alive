/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_07_certificates/04_07_01_certificates.sql
Version : 1.0
==============================================================

Description:
Creates the certificates table representing official course 
completion certificates awarded to users.

Dependencies:
- 03_types.sql
- 04_01_auth/04_01_auth_users.sql
- 04_03_courses/04_03_02_courses.sql
- 04_17_media.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS lms;

SET search_path TO lms, public;

------------------------------------------------------------
-- CERTIFICATES
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS lms.certificates
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id             UUID NOT NULL,
    course_id           UUID NOT NULL,

    certificate_number  VARCHAR(100) NOT NULL,

    status              lms.certificate_status NOT NULL DEFAULT 'GENERATED',

    issued_at           TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    media_id            UUID,

    grade               VARCHAR(10),

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
    CONSTRAINT uq_certificates_number
        UNIQUE(certificate_number),

    CONSTRAINT uq_certificates_user_course
        UNIQUE(user_id, course_id),

    CONSTRAINT fk_certificates_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_certificates_course
        FOREIGN KEY(course_id)
        REFERENCES lms.courses(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_certificates_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_certificates_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE lms.certificates IS
'Official course completion certificates awarded to students.';

COMMENT ON COLUMN lms.certificates.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN lms.certificates.certificate_number IS
'Unique public-facing certificate identifier.';

COMMENT ON COLUMN lms.certificates.status IS
'Status of the certificate (GENERATED, ISSUED, REVOKED).';

COMMENT ON COLUMN lms.certificates.media_id IS
'Reference to the generated PDF file of the certificate in the media library.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
