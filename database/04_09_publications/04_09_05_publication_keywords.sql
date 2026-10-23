/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_09_publications/04_09_05_publication_keywords.sql
Version : 1.0
==============================================================

Description:
Creates the lookup and mapping tables for tagging 
publications with searchable keywords.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_09_publications/04_09_02_publications.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS publication;

SET search_path TO publication, public;

------------------------------------------------------------
-- PUBLICATION KEYWORDS (Lookup Table)
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS publication.publication_keywords
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
    version             INTEGER NOT NULL DEFAULT 0,

    --------------------------------------------------------
    -- Constraints
    --------------------------------------------------------
    CONSTRAINT uq_publication_keywords
        UNIQUE(keyword),

    CONSTRAINT fk_publication_keywords_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_publication_keywords_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- PUBLICATION KEYWORD MAP (Many-to-Many)
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS publication.publication_keyword_map
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    publication_id      UUID NOT NULL,
    keyword_id          UUID NOT NULL,

    --------------------------------------------------------
    -- Audit Fields
    --------------------------------------------------------
    created_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at          TIMESTAMPTZ,
    created_by          UUID,
    updated_by          UUID,
    version             INTEGER NOT NULL DEFAULT 0,

    --------------------------------------------------------
    -- Constraints
    --------------------------------------------------------
    CONSTRAINT uq_publication_keyword_map
        UNIQUE(publication_id, keyword_id),

    CONSTRAINT fk_pkm_publication
        FOREIGN KEY(publication_id)
        REFERENCES publication.publications(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_pkm_keyword
        FOREIGN KEY(keyword_id)
        REFERENCES publication.publication_keywords(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_pkm_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_pkm_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE publication.publication_keywords IS
'Lookup table for searchable keywords/tags assigned to publications.';

COMMENT ON COLUMN publication.publication_keywords.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN publication.publication_keywords.keyword IS
'The unique keyword text.';

COMMENT ON TABLE publication.publication_keyword_map IS
'Many-to-many mapping table linking publications to keywords.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
