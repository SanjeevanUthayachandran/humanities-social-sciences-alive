/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_09_publications/04_09_09_publication_bookmarks.sql
Version : 1.0
==============================================================

Description:
Creates the publication_bookmarks table for users to save 
publications for later reading.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_09_publications/04_09_02_publications.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS publication;

SET search_path TO publication, public;

------------------------------------------------------------
-- PUBLICATION BOOKMARKS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS publication.publication_bookmarks
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    publication_id      UUID NOT NULL,
    user_id             UUID NOT NULL,

    --------------------------------------------------------
    --------------------------------------------------------
    -- Version
    --------------------------------------------------------
    version             INTEGER NOT NULL DEFAULT 0,

    -- Audit Fields (Append-only)
    --------------------------------------------------------
    created_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    --------------------------------------------------------
    -- Constraints
    --------------------------------------------------------
    CONSTRAINT uq_publication_bookmarks
        UNIQUE(publication_id, user_id),

    CONSTRAINT fk_publication_bookmarks_publication
        FOREIGN KEY(publication_id)
        REFERENCES publication.publications(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_publication_bookmarks_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE publication.publication_bookmarks IS
'Stores publications that users have saved/bookmarked for later.';

COMMENT ON COLUMN publication.publication_bookmarks.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN publication.publication_bookmarks.publication_id IS
'Reference to the saved publication.';

COMMENT ON COLUMN publication.publication_bookmarks.user_id IS
'Reference to the user who saved the publication.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
