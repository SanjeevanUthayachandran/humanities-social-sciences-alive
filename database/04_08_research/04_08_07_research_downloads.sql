/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_08_research/04_08_07_research_downloads.sql
Version : 1.0
==============================================================

Description:
Creates the research_downloads table to track and audit 
file downloads for analytics.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_08_research/04_08_04_research_files.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS research;

SET search_path TO research, public;

------------------------------------------------------------
-- RESEARCH DOWNLOADS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS research.research_downloads
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
    CONSTRAINT fk_research_downloads_file
        FOREIGN KEY(file_id)
        REFERENCES research.research_files(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_research_downloads_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_research_downloads_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE research.research_downloads IS
'Audit log tracking every time a research file is downloaded.';

COMMENT ON COLUMN research.research_downloads.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN research.research_downloads.file_id IS
'Reference to the downloaded research file.';

COMMENT ON COLUMN research.research_downloads.user_id IS
'User who downloaded it. Null if downloaded anonymously.';

COMMENT ON COLUMN research.research_downloads.ip_address IS
'IP address from which the download was requested.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
