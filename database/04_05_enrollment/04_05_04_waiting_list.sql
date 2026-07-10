/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_05_enrollment/04_05_04_waiting_list.sql
Version : 1.0
==============================================================

Description:
Creates the waiting_list table for tracking users who wish
to enroll in a full or upcoming course.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_03_courses/04_03_02_courses.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS lms;

SET search_path TO lms, public;

------------------------------------------------------------
-- WAITING LIST
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS lms.waiting_list
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    course_id           UUID NOT NULL,
    user_id             UUID NOT NULL,

    notified            BOOLEAN NOT NULL DEFAULT FALSE,
    notified_at         TIMESTAMPTZ,

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
    CONSTRAINT uq_waiting_list
        UNIQUE(course_id, user_id),

    CONSTRAINT fk_waiting_list_course
        FOREIGN KEY(course_id)
        REFERENCES lms.courses(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_waiting_list_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_waiting_list_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_waiting_list_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE lms.waiting_list IS
'Maintains a queue of users waiting to enroll in unavailable courses.';

COMMENT ON COLUMN lms.waiting_list.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN lms.waiting_list.course_id IS
'Reference to the requested course.';

COMMENT ON COLUMN lms.waiting_list.user_id IS
'Reference to the user on the waiting list.';

COMMENT ON COLUMN lms.waiting_list.notified IS
'True if the user has been notified that a spot is available.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
