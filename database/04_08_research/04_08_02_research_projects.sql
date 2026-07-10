/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_08_research/04_08_02_research_projects.sql
Version : 1.0
==============================================================

Description:
Creates the core research_projects table to house the main 
metadata and content for research items.

Dependencies:
- 03_types.sql
- 04_01_auth/04_01_auth_users.sql
- 04_08_research/04_08_01_research_categories.sql
- 04_17_media.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS research;

SET search_path TO research, public;

------------------------------------------------------------
-- RESEARCH PROJECTS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS research.research_projects
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    category_id         UUID NOT NULL,

    title               VARCHAR(255) NOT NULL,
    slug                VARCHAR(255) NOT NULL,

    abstract            TEXT,
    content             TEXT,

    research_type       research.research_type NOT NULL,
    status              research.research_status NOT NULL DEFAULT 'DRAFT',

    cover_media_id      UUID,

    published_date      TIMESTAMPTZ,

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
    CONSTRAINT uq_research_projects_slug
        UNIQUE(slug),

    CONSTRAINT fk_research_projects_category
        FOREIGN KEY(category_id)
        REFERENCES research.research_categories(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_research_projects_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_research_projects_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE research.research_projects IS
'Core table for managing research initiatives, articles, and theses.';

COMMENT ON COLUMN research.research_projects.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN research.research_projects.category_id IS
'Reference to the primary category grouping.';

COMMENT ON COLUMN research.research_projects.abstract IS
'Summary or abstract of the research project.';

COMMENT ON COLUMN research.research_projects.research_type IS
'Type of research (PROJECT, THESIS, ARTICLE, REPORT, BOOK).';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
