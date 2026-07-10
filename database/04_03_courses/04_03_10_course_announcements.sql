/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_03_courses/04_03_10_course_announcements.sql
Version : 1.0
==============================================================

Description:
Creates the course_announcements table to manage news and 
updates specific to a course.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_03_courses/04_03_02_courses.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS lms;

SET search_path TO lms, public;

------------------------------------------------------------
-- COURSE ANNOUNCEMENTS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS lms.course_announcements
(
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    course_id               UUID NOT NULL,

    title                   VARCHAR(255) NOT NULL,
    content                 TEXT NOT NULL,

    is_published            BOOLEAN NOT NULL DEFAULT TRUE,
    send_email_notification BOOLEAN NOT NULL DEFAULT FALSE,

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
    CONSTRAINT fk_course_announcements_course
        FOREIGN KEY(course_id)
        REFERENCES lms.courses(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_course_announcements_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_course_announcements_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE lms.course_announcements IS
'Stores announcements and updates broadcast to enrolled students of a specific course.';

COMMENT ON COLUMN lms.course_announcements.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN lms.course_announcements.course_id IS
'Reference to the associated course.';

COMMENT ON COLUMN lms.course_announcements.title IS
'Subject or title of the announcement.';

COMMENT ON COLUMN lms.course_announcements.content IS
'Detailed content/body of the announcement.';

COMMENT ON COLUMN lms.course_announcements.send_email_notification IS
'Flag indicating if this announcement should also trigger an email notification to students.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
