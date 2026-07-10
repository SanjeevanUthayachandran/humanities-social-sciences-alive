/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_10_workshops/04_10_05_workshop_resources.sql
Version : 1.0
==============================================================

Description:
Creates the workshop_resources table to attach handouts,
slides, or meeting links to a workshop.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_10_workshops/04_10_01_workshops.sql
- 04_17_media.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS event;

SET search_path TO event, public;

------------------------------------------------------------
-- WORKSHOP RESOURCES
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS event.workshop_resources
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    workshop_id         UUID NOT NULL,
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
    CONSTRAINT fk_wres_workshop
        FOREIGN KEY(workshop_id)
        REFERENCES event.workshops(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_wres_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_wres_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE event.workshop_resources IS
'Attachments (slides, handouts, PDFs) specifically associated with a workshop.';

COMMENT ON COLUMN event.workshop_resources.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN event.workshop_resources.workshop_id IS
'Reference to the parent workshop.';

COMMENT ON COLUMN event.workshop_resources.media_id IS
'Reference to the physical file stored in the media library.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
