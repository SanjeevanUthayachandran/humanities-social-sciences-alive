/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_05_enrollment/04_05_03_lesson_progress.sql
Version : 1.0
==============================================================

Description:
Creates the lesson_progress table to track a student's 
progress for individual lessons.

Dependencies:
- 03_types.sql
- 04_01_auth/04_01_auth_users.sql
- 04_04_lessons/04_04_02_lessons.sql
- 04_05_enrollment/04_05_01_enrollments.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS lms;

SET search_path TO lms, public;

------------------------------------------------------------
-- LESSON PROGRESS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS lms.lesson_progress
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    enrollment_id       UUID NOT NULL,
    lesson_id           UUID NOT NULL,

    status              lms.progress_status NOT NULL DEFAULT 'NOT_STARTED',

    completed_at        TIMESTAMPTZ,

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
    CONSTRAINT uq_lesson_progress
        UNIQUE(enrollment_id, lesson_id),

    CONSTRAINT fk_lesson_progress_enrollment
        FOREIGN KEY(enrollment_id)
        REFERENCES lms.enrollments(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_lesson_progress_lesson
        FOREIGN KEY(lesson_id)
        REFERENCES lms.lessons(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_lesson_progress_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_lesson_progress_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE lms.lesson_progress IS
'Tracks completion of individual lessons for an enrolled student.';

COMMENT ON COLUMN lms.lesson_progress.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN lms.lesson_progress.enrollment_id IS
'Reference to the user enrollment.';

COMMENT ON COLUMN lms.lesson_progress.lesson_id IS
'Reference to the specific lesson.';

COMMENT ON COLUMN lms.lesson_progress.status IS
'Status of the lesson (NOT_STARTED, IN_PROGRESS, COMPLETED).';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
