/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_08_research/04_08_05_research_keywords.sql
Version : 1.0
==============================================================

Description:
Creates the lookup and mapping tables for tagging research
projects with searchable keywords.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_08_research/04_08_02_research_projects.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS research;

SET search_path TO research, public;

------------------------------------------------------------
-- RESEARCH KEYWORDS (Lookup Table)
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS research.research_keywords
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    keyword             VARCHAR(100) NOT NULL,

    --------------------------------------------------------
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
    CONSTRAINT uq_research_keywords
        UNIQUE(keyword),

    CONSTRAINT fk_research_keywords_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_research_keywords_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- RESEARCH KEYWORD MAP (Many-to-Many)
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS research.research_keyword_map
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    project_id          UUID NOT NULL,
    keyword_id          UUID NOT NULL,

    --------------------------------------------------------
    -- Audit Fields
    --------------------------------------------------------
    created_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          UUID,

    --------------------------------------------------------
    -- Constraints
    --------------------------------------------------------
    CONSTRAINT uq_research_keyword_map
        UNIQUE(project_id, keyword_id),

    CONSTRAINT fk_rkm_project
        FOREIGN KEY(project_id)
        REFERENCES research.research_projects(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_rkm_keyword
        FOREIGN KEY(keyword_id)
        REFERENCES research.research_keywords(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_rkm_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE research.research_keywords IS
'Lookup table for searchable keywords/tags assigned to research projects.';

COMMENT ON COLUMN research.research_keywords.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN research.research_keywords.keyword IS
'The unique keyword text (e.g., "AI in Education").';

COMMENT ON TABLE research.research_keyword_map IS
'Many-to-many mapping table linking research projects to keywords.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
