/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_09_publications/04_09_03_publication_authors.sql
Version : 1.0
==============================================================

Description:
Creates the publication_authors mapping table to link multiple 
contributors (users) to a single publication.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_09_publications/04_09_02_publications.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS publication;

SET search_path TO publication, public;

------------------------------------------------------------
-- PUBLICATION AUTHORS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS publication.publication_authors
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    publication_id      UUID NOT NULL,
    user_id             UUID NOT NULL,

    is_primary          BOOLEAN NOT NULL DEFAULT FALSE,
    display_order       INTEGER NOT NULL DEFAULT 0,

    --------------------------------------------------------
    --------------------------------------------------------
    -- Version
    --------------------------------------------------------
    version             INTEGER NOT NULL DEFAULT 0,

    -- Audit Fields
    --------------------------------------------------------
    created_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          UUID,

    --------------------------------------------------------
    -- Constraints
    --------------------------------------------------------
    CONSTRAINT uq_publication_authors
        UNIQUE(publication_id, user_id),

    CONSTRAINT fk_publication_authors_publication
        FOREIGN KEY(publication_id)
        REFERENCES publication.publications(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_publication_authors_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_publication_authors_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE publication.publication_authors IS
'Maps authors/contributors to a specific publication.';

COMMENT ON COLUMN publication.publication_authors.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN publication.publication_authors.publication_id IS
'Reference to the publication.';

COMMENT ON COLUMN publication.publication_authors.user_id IS
'Reference to the contributing user.';

COMMENT ON COLUMN publication.publication_authors.is_primary IS
'True if the user is the primary or lead author.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
