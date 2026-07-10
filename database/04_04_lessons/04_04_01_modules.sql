/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_04_lessons/04_04_01_modules.sql
Version : 1.0
==============================================================

Description:
Creates the modules table to group lessons logically within a course.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_03_courses/04_03_02_courses.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS lms;

SET search_path TO lms, public;

------------------------------------------------------------
-- MODULES
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS lms.modules
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    course_id           UUID NOT NULL,

    title               VARCHAR(255) NOT NULL,
    description         TEXT,

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
    CONSTRAINT fk_modules_course
        FOREIGN KEY(course_id)
        REFERENCES lms.courses(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_modules_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_modules_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE lms.modules IS
'Logical grouping of lessons within a course (e.g., Week 1, Chapter 1).';

COMMENT ON COLUMN lms.modules.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN lms.modules.course_id IS
'Reference to the parent course.';

COMMENT ON COLUMN lms.modules.title IS
'Title of the module.';

COMMENT ON COLUMN lms.modules.display_order IS
'Sorting order of the module within the course.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
