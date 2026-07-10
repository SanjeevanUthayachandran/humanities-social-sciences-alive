/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_09_publications/04_09_02_publications.sql
Version : 1.0
==============================================================

Description:
Creates the core publications table to house the main 
metadata and content for publication items.

Dependencies:
- 03_types.sql
- 04_01_auth/04_01_auth_users.sql
- 04_09_publications/04_09_01_publication_categories.sql
- 04_17_media.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS publication;

SET search_path TO publication, public;

------------------------------------------------------------
-- PUBLICATIONS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS publication.publications
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    category_id         UUID NOT NULL,

    title               VARCHAR(255) NOT NULL,
    slug                VARCHAR(255) NOT NULL,

    abstract            TEXT,
    content             TEXT,

    publication_type    publication.publication_type NOT NULL,
    status              publication.publication_status NOT NULL DEFAULT 'DRAFT',

    cover_media_id      UUID,

    published_date      TIMESTAMPTZ,
    doi                 VARCHAR(100),

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
    CONSTRAINT uq_publications_slug
        UNIQUE(slug),

    CONSTRAINT fk_publications_category
        FOREIGN KEY(category_id)
        REFERENCES publication.publication_categories(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_publications_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_publications_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE publication.publications IS
'Core table for managing publications like journals, books, and articles.';

COMMENT ON COLUMN publication.publications.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN publication.publications.category_id IS
'Reference to the primary category grouping.';

COMMENT ON COLUMN publication.publications.abstract IS
'Summary or abstract of the publication.';

COMMENT ON COLUMN publication.publications.publication_type IS
'Type of publication (JOURNAL, BOOK, ARTICLE, etc.).';

COMMENT ON COLUMN publication.publications.doi IS
'Digital Object Identifier for academic publications.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
