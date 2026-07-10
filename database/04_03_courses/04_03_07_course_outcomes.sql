/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_03_courses/04_03_07_course_outcomes.sql
Version : 1.0
==============================================================

Description:
Creates the course_outcomes table to define the learning 
objectives or expected outcomes of a course.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_03_courses/04_03_02_courses.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS lms;

SET search_path TO lms, public;

------------------------------------------------------------
-- COURSE OUTCOMES
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS lms.course_outcomes
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    course_id           UUID NOT NULL,

    outcome_text        TEXT NOT NULL,

    display_order       INTEGER NOT NULL DEFAULT 0,

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
    CONSTRAINT fk_course_outcomes_course
        FOREIGN KEY(course_id)
        REFERENCES lms.courses(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_course_outcomes_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_course_outcomes_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE lms.course_outcomes IS
'Defines the expected learning objectives or outcomes upon completing a course.';

COMMENT ON COLUMN lms.course_outcomes.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN lms.course_outcomes.course_id IS
'Reference to the course.';

COMMENT ON COLUMN lms.course_outcomes.outcome_text IS
'The actual learning outcome text (e.g., "Understand basic macroeconomic principles").';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
