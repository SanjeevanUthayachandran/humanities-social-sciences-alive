/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_03_courses/04_03_03_course_instructors.sql
Version : 1.0
==============================================================

Description:
Creates the course_instructors mapping table to assign 
instructors to courses.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_03_courses/04_03_02_courses.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS lms;

SET search_path TO lms, public;

------------------------------------------------------------
-- COURSE INSTRUCTORS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS lms.course_instructors
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    course_id           UUID NOT NULL,
    instructor_id       UUID NOT NULL,

    is_primary          BOOLEAN NOT NULL DEFAULT FALSE,
    display_order       INTEGER NOT NULL DEFAULT 0,

    --------------------------------------------------------
    --------------------------------------------------------
    -- Version
    --------------------------------------------------------
    version             INTEGER NOT NULL DEFAULT 0,

    -- Audit Fields
    --------------------------------------------------------
    created_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          UUID,

    --------------------------------------------------------
    -- Constraints
    --------------------------------------------------------
    CONSTRAINT uq_course_instructors
        UNIQUE(course_id, instructor_id),

    CONSTRAINT fk_course_instructors_course
        FOREIGN KEY(course_id)
        REFERENCES lms.courses(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_course_instructors_user
        FOREIGN KEY(instructor_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_course_instructors_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE lms.course_instructors IS
'Mapping table assigning one or more instructors to a course.';

COMMENT ON COLUMN lms.course_instructors.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN lms.course_instructors.course_id IS
'Reference to the course.';

COMMENT ON COLUMN lms.course_instructors.instructor_id IS
'Reference to the instructor user account.';

COMMENT ON COLUMN lms.course_instructors.is_primary IS
'True if this instructor is the primary/lead instructor for the course.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
