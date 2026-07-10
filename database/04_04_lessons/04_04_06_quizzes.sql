/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_04_lessons/04_04_06_quizzes.sql
Version : 1.0
==============================================================

Description:
Creates the quizzes table.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_04_lessons/04_04_02_lessons.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS lms;

SET search_path TO lms, public;

------------------------------------------------------------
-- QUIZZES
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS lms.quizzes
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    lesson_id           UUID NOT NULL,

    title               VARCHAR(255) NOT NULL,
    description         TEXT,

    time_limit_minutes  INTEGER,
    total_marks         INTEGER,
    pass_marks          INTEGER,

    max_attempts        INTEGER NOT NULL DEFAULT 1,

    is_active           BOOLEAN NOT NULL DEFAULT TRUE,

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
    CONSTRAINT uq_quizzes_lesson
        UNIQUE(lesson_id),

    CONSTRAINT fk_quizzes_lesson
        FOREIGN KEY(lesson_id)
        REFERENCES lms.lessons(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_quizzes_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_quizzes_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_quizzes_marks
        CHECK (total_marks >= 0 AND pass_marks >= 0 AND pass_marks <= total_marks),

    CONSTRAINT chk_quizzes_time
        CHECK (time_limit_minutes >= 0),

    CONSTRAINT chk_quizzes_attempts
        CHECK (max_attempts > 0)
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE lms.quizzes IS
'Defines quizzes tied to specific lessons.';

COMMENT ON COLUMN lms.quizzes.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN lms.quizzes.lesson_id IS
'Reference to the parent lesson. Unique constraint enforces one quiz per QUIZ-type lesson.';

COMMENT ON COLUMN lms.quizzes.time_limit_minutes IS
'Time limit to complete the quiz. NULL implies no limit.';

COMMENT ON COLUMN lms.quizzes.max_attempts IS
'Maximum number of attempts allowed for a user. Defaults to 1.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
