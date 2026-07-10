/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_08_research/04_08_03_research_authors.sql
Version : 1.0
==============================================================

Description:
Creates the research_authors mapping table to link multiple 
contributors (users) to a single research project.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_08_research/04_08_02_research_projects.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS research;

SET search_path TO research, public;

------------------------------------------------------------
-- RESEARCH AUTHORS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS research.research_authors
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    project_id          UUID NOT NULL,
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
    CONSTRAINT uq_research_authors
        UNIQUE(project_id, user_id),

    CONSTRAINT fk_research_authors_project
        FOREIGN KEY(project_id)
        REFERENCES research.research_projects(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_research_authors_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_research_authors_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE research.research_authors IS
'Maps authors/contributors to a specific research project.';

COMMENT ON COLUMN research.research_authors.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN research.research_authors.project_id IS
'Reference to the research project.';

COMMENT ON COLUMN research.research_authors.user_id IS
'Reference to the contributing user.';

COMMENT ON COLUMN research.research_authors.is_primary IS
'True if the user is the primary or lead author.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
