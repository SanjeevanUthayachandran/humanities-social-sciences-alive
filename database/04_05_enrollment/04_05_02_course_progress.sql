/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_05_enrollment/04_05_02_course_progress.sql
Version : 1.0
==============================================================

Description:
Creates the course_progress table to track a student's 
overall progress in an enrolled course.

Dependencies:
- 03_types.sql
- 04_01_auth/04_01_auth_users.sql
- 04_05_enrollment/04_05_01_enrollments.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS lms;

SET search_path TO lms, public;

------------------------------------------------------------
-- COURSE PROGRESS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS lms.course_progress
(
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    enrollment_id           UUID NOT NULL,

    status                  lms.progress_status NOT NULL DEFAULT 'NOT_STARTED',

    completion_percentage   NUMERIC(5, 2) NOT NULL DEFAULT 0.00,

    last_accessed_at        TIMESTAMPTZ,

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
    CONSTRAINT uq_course_progress_enrollment
        UNIQUE(enrollment_id),

    CONSTRAINT fk_course_progress_enrollment
        FOREIGN KEY(enrollment_id)
        REFERENCES lms.enrollments(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_course_progress_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_course_progress_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_course_progress_percentage
        CHECK (completion_percentage >= 0 AND completion_percentage <= 100)
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE lms.course_progress IS
'Tracks the overall completion progress for an enrollment.';

COMMENT ON COLUMN lms.course_progress.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN lms.course_progress.enrollment_id IS
'Reference to the associated enrollment.';

COMMENT ON COLUMN lms.course_progress.completion_percentage IS
'Calculated completion percentage from 0.00 to 100.00.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
