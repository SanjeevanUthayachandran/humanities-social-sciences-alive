/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_05_enrollment/04_05_01_enrollments.sql
Version : 1.0
==============================================================

Description:
Creates the enrollments table to track users enrolled in courses.

Dependencies:
- 03_types.sql
- 04_01_auth/04_01_auth_users.sql
- 04_03_courses/04_03_02_courses.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS lms;

SET search_path TO lms, public;

------------------------------------------------------------
-- ENROLLMENTS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS lms.enrollments
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id             UUID NOT NULL,
    course_id           UUID NOT NULL,

    status              lms.enrollment_status NOT NULL DEFAULT 'PENDING',

    enrolled_at         TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at          TIMESTAMPTZ,

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
    CONSTRAINT uq_enrollments
        UNIQUE(user_id, course_id),

    CONSTRAINT fk_enrollments_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_enrollments_course
        FOREIGN KEY(course_id)
        REFERENCES lms.courses(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_enrollments_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_enrollments_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE lms.enrollments IS
'Tracks user enrollments in specific courses.';

COMMENT ON COLUMN lms.enrollments.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN lms.enrollments.user_id IS
'Reference to the enrolled student.';

COMMENT ON COLUMN lms.enrollments.course_id IS
'Reference to the enrolled course.';

COMMENT ON COLUMN lms.enrollments.status IS
'Status of the enrollment (e.g., ENROLLED, COMPLETED, CANCELLED).';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
