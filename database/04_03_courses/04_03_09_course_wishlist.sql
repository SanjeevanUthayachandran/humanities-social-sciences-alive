/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_03_courses/04_03_09_course_wishlist.sql
Version : 1.0
==============================================================

Description:
Creates the course_wishlist table to store courses that 
users have saved for later.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_03_courses/04_03_02_courses.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS lms;

SET search_path TO lms, public;

------------------------------------------------------------
-- COURSE WISHLIST
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS lms.course_wishlist
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    course_id           UUID NOT NULL,
    user_id             UUID NOT NULL,

    --------------------------------------------------------
    --------------------------------------------------------
    -- Version
    --------------------------------------------------------
    version             INTEGER NOT NULL DEFAULT 0,

    -- Audit Fields
    --------------------------------------------------------
    created_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    --------------------------------------------------------
    -- Constraints
    --------------------------------------------------------
    CONSTRAINT uq_course_wishlist
        UNIQUE(course_id, user_id),

    CONSTRAINT fk_course_wishlist_course
        FOREIGN KEY(course_id)
        REFERENCES lms.courses(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_course_wishlist_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE lms.course_wishlist IS
'Stores courses that students have saved/bookmarked for later.';

COMMENT ON COLUMN lms.course_wishlist.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN lms.course_wishlist.course_id IS
'Reference to the saved course.';

COMMENT ON COLUMN lms.course_wishlist.user_id IS
'Reference to the user who saved the course.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
