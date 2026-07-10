/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_09_publications/04_09_04_publication_files.sql
Version : 1.0
==============================================================

Description:
Creates the publication_files table for attaching documents,
datasets, and PDFs directly to a publication.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_09_publications/04_09_02_publications.sql
- 04_17_media.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS publication;

SET search_path TO publication, public;

------------------------------------------------------------
-- PUBLICATION FILES
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS publication.publication_files
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    publication_id      UUID NOT NULL,
    media_id            UUID NOT NULL,

    title               VARCHAR(255) NOT NULL,
    
    is_public           BOOLEAN NOT NULL DEFAULT TRUE,
    display_order       INTEGER NOT NULL DEFAULT 0,

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
    CONSTRAINT fk_publication_files_publication
        FOREIGN KEY(publication_id)
        REFERENCES publication.publications(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_publication_files_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_publication_files_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE publication.publication_files IS
'Attachments (documents, datasets, images) explicitly associated with a publication.';

COMMENT ON COLUMN publication.publication_files.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN publication.publication_files.publication_id IS
'Reference to the parent publication.';

COMMENT ON COLUMN publication.publication_files.media_id IS
'Reference to the physical file stored in the media library.';

COMMENT ON COLUMN publication.publication_files.is_public IS
'Determines if the file can be downloaded by anonymous users.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
