/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_08_research/04_08_01_research_categories.sql
Version : 1.0
==============================================================

Description:
Creates the research_categories table to classify research 
projects and publications.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS research;

SET search_path TO research, public;

------------------------------------------------------------
-- RESEARCH CATEGORIES
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS research.research_categories
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    name                VARCHAR(100) NOT NULL,
    slug                VARCHAR(100) NOT NULL,

    description         TEXT,

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
    CONSTRAINT uq_research_categories_name
        UNIQUE(name),

    CONSTRAINT uq_research_categories_slug
        UNIQUE(slug),

    CONSTRAINT fk_research_categories_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_research_categories_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE research.research_categories IS
'High-level classification for research projects (e.g., Sociology, Literature).';

COMMENT ON COLUMN research.research_categories.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN research.research_categories.name IS
'Display name of the category.';

COMMENT ON COLUMN research.research_categories.slug IS
'URL-friendly string.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
