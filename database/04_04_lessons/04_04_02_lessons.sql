/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_04_lessons/04_04_02_lessons.sql
Version : 1.0
==============================================================

Description:
Creates the lessons table which stores the actual learning content.

Dependencies:
- 03_types.sql
- 04_01_auth/04_01_auth_users.sql
- 04_04_lessons/04_04_01_modules.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS lms;

SET search_path TO lms, public;

------------------------------------------------------------
-- LESSONS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS lms.lessons
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    module_id           UUID NOT NULL,

    title               VARCHAR(255) NOT NULL,
    slug                VARCHAR(255) NOT NULL,
    
    description         TEXT,
    content             TEXT,

    lesson_type         lms.lesson_type NOT NULL,

    duration_minutes    INTEGER,

    is_free_preview     BOOLEAN NOT NULL DEFAULT FALSE,
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
    CONSTRAINT uq_lessons_slug
        UNIQUE(slug),

    CONSTRAINT fk_lessons_module
        FOREIGN KEY(module_id)
        REFERENCES lms.modules(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_lessons_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_lessons_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_lessons_duration
        CHECK (duration_minutes >= 0)
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE lms.lessons IS
'Stores individual lessons or learning units within a module.';

COMMENT ON COLUMN lms.lessons.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN lms.lessons.module_id IS
'Reference to the parent module.';

COMMENT ON COLUMN lms.lessons.slug IS
'URL-friendly string for the lesson.';

COMMENT ON COLUMN lms.lessons.lesson_type IS
'Type of content (VIDEO, PDF, TEXT, QUIZ, etc.).';

COMMENT ON COLUMN lms.lessons.content IS
'Rich text content if the lesson type is TEXT or embedded content.';

COMMENT ON COLUMN lms.lessons.is_free_preview IS
'If true, non-enrolled users can view this lesson as a preview.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
