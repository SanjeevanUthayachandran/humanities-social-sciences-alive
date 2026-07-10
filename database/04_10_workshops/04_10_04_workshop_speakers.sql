/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_10_workshops/04_10_04_workshop_speakers.sql
Version : 1.0
==============================================================

Description:
Creates the workshop_speakers table to link multiple speakers 
or organizers to a single workshop.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_10_workshops/04_10_01_workshops.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS event;

SET search_path TO event, public;

------------------------------------------------------------
-- WORKSHOP SPEAKERS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS event.workshop_speakers
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    workshop_id         UUID NOT NULL,
    speaker_id          UUID NOT NULL,

    bio                 TEXT,
    is_keynote          BOOLEAN NOT NULL DEFAULT FALSE,

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
    CONSTRAINT uq_workshop_speakers
        UNIQUE(workshop_id, speaker_id),

    CONSTRAINT fk_wsp_workshop
        FOREIGN KEY(workshop_id)
        REFERENCES event.workshops(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_wsp_user
        FOREIGN KEY(speaker_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_wsp_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE event.workshop_speakers IS
'Maps speakers/panelists to an overall workshop.';

COMMENT ON COLUMN event.workshop_speakers.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN event.workshop_speakers.workshop_id IS
'Reference to the workshop.';

COMMENT ON COLUMN event.workshop_speakers.speaker_id IS
'Reference to the speaker user account.';

COMMENT ON COLUMN event.workshop_speakers.is_keynote IS
'True if the speaker is a keynote/main speaker.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
