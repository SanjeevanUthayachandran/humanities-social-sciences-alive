/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_04_lessons/04_04_04_lesson_videos.sql
Version : 1.0
==============================================================

Description:
Creates the lesson_videos table. Instead of storing a direct URL,
it references the unified media.media_files table to retrieve
video paths or external provider IDs (YouTube/Vimeo).

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
-- LESSON VIDEOS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS lms.lesson_videos
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    lesson_id           UUID NOT NULL,

    media_id            UUID NOT NULL,

    duration_seconds    INTEGER,
    is_downloadable     BOOLEAN NOT NULL DEFAULT FALSE,

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
    CONSTRAINT uq_lesson_videos_lesson
        UNIQUE(lesson_id),

    CONSTRAINT fk_lesson_videos_lesson
        FOREIGN KEY(lesson_id)
        REFERENCES lms.lessons(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_lesson_videos_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_lesson_videos_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_lesson_videos_duration
        CHECK (duration_seconds >= 0)
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE lms.lesson_videos IS
'Associates video media files directly to lessons. Typically one video per VIDEO-type lesson.';

COMMENT ON COLUMN lms.lesson_videos.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN lms.lesson_videos.lesson_id IS
'Reference to the parent lesson. Unique constraint enforces one main video per lesson.';

COMMENT ON COLUMN lms.lesson_videos.media_id IS
'Reference to the actual video record in the media library (which handles local, S3, YouTube, Vimeo etc.).';

COMMENT ON COLUMN lms.lesson_videos.duration_seconds IS
'Cached duration of the video in seconds for quick retrieval.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
