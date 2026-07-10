/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_04_lessons/04_04_05_assignments.sql
Version : 1.0
==============================================================

Description:
Creates the assignments table.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_17_media.sql
- 04_04_lessons/04_04_02_lessons.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS lms;

SET search_path TO lms, public;

------------------------------------------------------------
-- ASSIGNMENTS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS lms.assignments
(
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    lesson_id               UUID NOT NULL,

    title                   VARCHAR(255) NOT NULL,
    description             TEXT NOT NULL,

    instruction_media_id    UUID,

    total_marks             INTEGER,
    pass_marks              INTEGER,

    deadline                TIMESTAMPTZ,

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
    CONSTRAINT uq_assignments_lesson
        UNIQUE(lesson_id),

    CONSTRAINT fk_assignments_lesson
        FOREIGN KEY(lesson_id)
        REFERENCES lms.lessons(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_assignments_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_assignments_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_assignments_marks
        CHECK (total_marks >= 0 AND pass_marks >= 0 AND pass_marks <= total_marks)
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE lms.assignments IS
'Defines assignment tasks tied to specific lessons.';

COMMENT ON COLUMN lms.assignments.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN lms.assignments.lesson_id IS
'Reference to the parent lesson. Unique constraint enforces one assignment per ASSIGNMENT-type lesson.';

COMMENT ON COLUMN lms.assignments.instruction_media_id IS
'Reference to a file in the media library containing assignment instructions or resources.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
