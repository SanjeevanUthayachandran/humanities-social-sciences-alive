/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_05_enrollment/04_05_05_bookmarks.sql
Version : 1.0
==============================================================

Description:
Creates the bookmarks table for users to save and annotate 
specific moments in lessons/videos.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_04_lessons/04_04_02_lessons.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS lms;

SET search_path TO lms, public;

------------------------------------------------------------
-- BOOKMARKS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS lms.bookmarks
(
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id                 UUID NOT NULL,
    lesson_id               UUID NOT NULL,

    note                    TEXT,
    video_timestamp_seconds INTEGER,

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
    CONSTRAINT fk_bookmarks_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_bookmarks_lesson
        FOREIGN KEY(lesson_id)
        REFERENCES lms.lessons(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_bookmarks_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_bookmarks_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_bookmarks_timestamp
        CHECK (video_timestamp_seconds >= 0)
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE lms.bookmarks IS
'Student-created bookmarks and notes mapped to specific lessons.';

COMMENT ON COLUMN lms.bookmarks.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN lms.bookmarks.user_id IS
'Reference to the user who created the bookmark.';

COMMENT ON COLUMN lms.bookmarks.lesson_id IS
'Reference to the bookmarked lesson.';

COMMENT ON COLUMN lms.bookmarks.video_timestamp_seconds IS
'Optional timestamp for jumping to a specific point in a video lesson.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
