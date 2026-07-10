/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_03_courses/04_03_06_course_prerequisites.sql
Version : 1.0
==============================================================

Description:
Creates the course_prerequisites table to manage course 
dependencies.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_03_courses/04_03_02_courses.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS lms;

SET search_path TO lms, public;

------------------------------------------------------------
-- COURSE PREREQUISITES
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS lms.course_prerequisites
(
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    course_id               UUID NOT NULL,
    prerequisite_course_id  UUID NOT NULL,

    is_mandatory            BOOLEAN NOT NULL DEFAULT TRUE,

    --------------------------------------------------------
    --------------------------------------------------------
    -- Version
    --------------------------------------------------------
    version             INTEGER NOT NULL DEFAULT 0,

    -- Audit Fields
    --------------------------------------------------------
    created_at              TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    
    created_by              UUID,
    

    --------------------------------------------------------
    -- Constraints
    --------------------------------------------------------
    CONSTRAINT uq_course_prerequisites
        UNIQUE(course_id, prerequisite_course_id),

    CONSTRAINT chk_course_prerequisites_self
        CHECK (course_id != prerequisite_course_id),

    CONSTRAINT fk_course_prerequisites_course
        FOREIGN KEY(course_id)
        REFERENCES lms.courses(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_course_prerequisites_required
        FOREIGN KEY(prerequisite_course_id)
        REFERENCES lms.courses(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_course_prereq_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE lms.course_prerequisites IS
'Tracks courses that must or should be completed before enrolling in another course.';

COMMENT ON COLUMN lms.course_prerequisites.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN lms.course_prerequisites.course_id IS
'The course that has a prerequisite requirement.';

COMMENT ON COLUMN lms.course_prerequisites.prerequisite_course_id IS
'The required course to be completed first.';

COMMENT ON COLUMN lms.course_prerequisites.is_mandatory IS
'True if the prerequisite is strictly enforced; False if it is only recommended.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
