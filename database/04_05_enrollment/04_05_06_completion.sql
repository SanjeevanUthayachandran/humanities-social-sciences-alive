/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_05_enrollment/04_05_06_completion.sql
Version : 1.0
==============================================================

Description:
Creates the completion table to permanently record when 
an enrollment successfully completes a course.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_05_enrollment/04_05_01_enrollments.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS lms;

SET search_path TO lms, public;

------------------------------------------------------------
-- COMPLETION
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS lms.completion
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    enrollment_id       UUID NOT NULL,

    completed_at        TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    certificate_issued  BOOLEAN NOT NULL DEFAULT FALSE,

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
    CONSTRAINT uq_completion_enrollment
        UNIQUE(enrollment_id),

    CONSTRAINT fk_completion_enrollment
        FOREIGN KEY(enrollment_id)
        REFERENCES lms.enrollments(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_completion_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_completion_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE lms.completion IS
'Permanent records of course completion events and final grades.';

COMMENT ON COLUMN lms.completion.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN lms.completion.enrollment_id IS
'Reference to the enrollment that was completed.';

COMMENT ON COLUMN lms.completion.completed_at IS
'Exact timestamp when the course was successfully completed.';

COMMENT ON COLUMN lms.completion.certificate_issued IS
'True if a certificate has been successfully issued for this completion.';

COMMENT ON COLUMN lms.completion.grade IS
'Optional overall grade awarded upon completion.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
