/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_08_research/04_08_06_research_citations.sql
Version : 1.0
==============================================================

Description:
Creates the research_citations table to explicitly store 
formatted citations (APA, MLA) for a research project.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_08_research/04_08_02_research_projects.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS research;

SET search_path TO research, public;

------------------------------------------------------------
-- RESEARCH CITATIONS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS research.research_citations
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    project_id          UUID NOT NULL,

    citation_format     VARCHAR(50) NOT NULL, -- e.g., 'APA', 'MLA', 'CHICAGO'
    citation_text       TEXT NOT NULL,

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
    CONSTRAINT uq_research_citations_format
        UNIQUE(project_id, citation_format),

    CONSTRAINT fk_research_citations_project
        FOREIGN KEY(project_id)
        REFERENCES research.research_projects(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_research_citations_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_research_citations_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE research.research_citations IS
'Pre-formatted citation strings for a project (e.g., APA, MLA formats).';

COMMENT ON COLUMN research.research_citations.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN research.research_citations.project_id IS
'Reference to the research project being cited.';

COMMENT ON COLUMN research.research_citations.citation_format IS
'Standardized name of the format (e.g., APA, MLA, IEEE).';

COMMENT ON COLUMN research.research_citations.citation_text IS
'The full, accurately formatted citation text block.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
