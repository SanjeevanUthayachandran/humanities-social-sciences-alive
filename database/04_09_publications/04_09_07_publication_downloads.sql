/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_09_publications/04_09_07_publication_downloads.sql
Version : 1.0
==============================================================

Description:
Creates the publication_downloads table to track and audit 
file downloads for analytics.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_09_publications/04_09_04_publication_files.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS publication;

SET search_path TO publication, public;

------------------------------------------------------------
-- PUBLICATION DOWNLOADS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS publication.publication_downloads
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    file_id             UUID NOT NULL,
    user_id             UUID,

    ip_address          VARCHAR(45),
    downloaded_at       TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    --------------------------------------------------------
    --------------------------------------------------------
    -- Version
    --------------------------------------------------------
    version             INTEGER NOT NULL DEFAULT 0,

    -- Audit Fields (Append-only)
    --------------------------------------------------------
    created_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          UUID,

    --------------------------------------------------------
    -- Constraints
    --------------------------------------------------------
    CONSTRAINT fk_publication_downloads_file
        FOREIGN KEY(file_id)
        REFERENCES publication.publication_files(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_publication_downloads_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_publication_downloads_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE publication.publication_downloads IS
'Audit log tracking every time a publication file is downloaded.';

COMMENT ON COLUMN publication.publication_downloads.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN publication.publication_downloads.file_id IS
'Reference to the downloaded publication file.';

COMMENT ON COLUMN publication.publication_downloads.user_id IS
'User who downloaded it. Null if downloaded anonymously.';

COMMENT ON COLUMN publication.publication_downloads.ip_address IS
'IP address from which the download was requested.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
