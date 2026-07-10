/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_09_publications/04_09_06_publication_citations.sql
Version : 1.0
==============================================================

Description:
Creates the publication_citations table to explicitly store 
formatted citations (APA, MLA) for a publication.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_09_publications/04_09_02_publications.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS publication;

SET search_path TO publication, public;

------------------------------------------------------------
-- PUBLICATION CITATIONS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS publication.publication_citations
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    publication_id      UUID NOT NULL,

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
    CONSTRAINT uq_publication_citations_format
        UNIQUE(publication_id, citation_format),

    CONSTRAINT fk_publication_citations_publication
        FOREIGN KEY(publication_id)
        REFERENCES publication.publications(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_publication_citations_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_publication_citations_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE publication.publication_citations IS
'Pre-formatted citation strings for a publication (e.g., APA, MLA formats).';

COMMENT ON COLUMN publication.publication_citations.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN publication.publication_citations.publication_id IS
'Reference to the publication being cited.';

COMMENT ON COLUMN publication.publication_citations.citation_format IS
'Standardized name of the format (e.g., APA, MLA, IEEE).';

COMMENT ON COLUMN publication.publication_citations.citation_text IS
'The full, accurately formatted citation text block.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
