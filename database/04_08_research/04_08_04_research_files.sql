/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_08_research/04_08_04_research_files.sql
Version : 1.0
==============================================================

Description:
Creates the research_files table for attaching documents,
datasets, and PDFs directly to a research project.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_08_research/04_08_02_research_projects.sql
- 04_17_media.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS research;

SET search_path TO research, public;

------------------------------------------------------------
-- RESEARCH FILES
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS research.research_files
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    project_id          UUID NOT NULL,
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
    CONSTRAINT fk_research_files_project
        FOREIGN KEY(project_id)
        REFERENCES research.research_projects(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_research_files_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_research_files_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE research.research_files IS
'Attachments (documents, datasets, images) explicitly associated with a research project.';

COMMENT ON COLUMN research.research_files.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN research.research_files.project_id IS
'Reference to the parent research project.';

COMMENT ON COLUMN research.research_files.media_id IS
'Reference to the physical file stored in the media library.';

COMMENT ON COLUMN research.research_files.is_public IS
'Determines if the file can be downloaded by anonymous users.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
