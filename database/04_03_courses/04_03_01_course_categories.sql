/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_03_courses/04_03_01_course_categories.sql
Version : 1.0
==============================================================

Description:
Creates the course_categories table for LMS module.

==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS lms;

SET search_path TO lms, public;

------------------------------------------------------------
-- COURSE CATEGORIES
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS lms.course_categories
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    parent_category_id  UUID,

    name                VARCHAR(150) NOT NULL,
    slug                VARCHAR(150) NOT NULL,

    description         TEXT,

    icon_media_id       UUID,

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
    CONSTRAINT uq_course_categories_slug
        UNIQUE(slug),

    CONSTRAINT fk_course_categories_parent
        FOREIGN KEY(parent_category_id)
        REFERENCES lms.course_categories(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_course_categories_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_course_categories_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE lms.course_categories IS
'Hierarchical categories used to organize educational courses.';

COMMENT ON COLUMN lms.course_categories.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN lms.course_categories.parent_category_id IS
'Self-referencing foreign key for subcategories. NULL if top-level.';

COMMENT ON COLUMN lms.course_categories.slug IS
'URL-friendly string for the category.';

COMMENT ON COLUMN lms.course_categories.icon_media_id IS
'Reference to the category icon in media library.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
