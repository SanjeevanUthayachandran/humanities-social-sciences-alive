/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_04_lessons/04_04_03_lesson_resources.sql
Version : 1.0
==============================================================

Description:
Creates the lesson_resources table for supplementary materials
specific to a single lesson.

Dependencies:
- 03_types.sql
- 04_01_auth/04_01_auth_users.sql
- 04_17_media.sql
- 04_04_lessons/04_04_02_lessons.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS lms;

SET search_path TO lms, public;

------------------------------------------------------------
-- LESSON RESOURCES
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS lms.lesson_resources
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    lesson_id           UUID NOT NULL,

    title               VARCHAR(255) NOT NULL,
    resource_type       lms.resource_type NOT NULL,

    --------------------------------------------------------
    -- File / Link Reference
    --------------------------------------------------------
    media_id            UUID,
    external_link       TEXT,

    is_downloadable     BOOLEAN NOT NULL DEFAULT TRUE,
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
    CONSTRAINT fk_lesson_resources_lesson
        FOREIGN KEY(lesson_id)
        REFERENCES lms.lessons(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_lesson_resources_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_lesson_resources_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_lesson_resource_source
        CHECK (
            (media_id IS NOT NULL AND external_link IS NULL) OR
            (media_id IS NULL AND external_link IS NOT NULL)
        )
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE lms.lesson_resources IS
'Supplementary materials attached specifically to a lesson.';

COMMENT ON COLUMN lms.lesson_resources.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN lms.lesson_resources.lesson_id IS
'Reference to the associated lesson.';

COMMENT ON COLUMN lms.lesson_resources.media_id IS
'Reference to the physical file in the media library.';

COMMENT ON COLUMN lms.lesson_resources.external_link IS
'External URL if the resource is an external link rather than a hosted file.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
