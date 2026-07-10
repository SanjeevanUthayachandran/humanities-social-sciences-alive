/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_11_events/04_11_04_event_speakers.sql
Version : 1.0
==============================================================

Description:
Creates the event_speakers table to link multiple speakers 
or organizers to a single event.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_11_events/04_11_01_events.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS event;

SET search_path TO event, public;

------------------------------------------------------------
-- EVENT SPEAKERS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS event.event_speakers
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    event_id            UUID NOT NULL,
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
    CONSTRAINT uq_event_speakers
        UNIQUE(event_id, speaker_id),

    CONSTRAINT fk_esp_event
        FOREIGN KEY(event_id)
        REFERENCES event.events(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_esp_user
        FOREIGN KEY(speaker_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_esp_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE event.event_speakers IS
'Maps speakers/panelists to an overall event.';

COMMENT ON COLUMN event.event_speakers.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN event.event_speakers.event_id IS
'Reference to the event.';

COMMENT ON COLUMN event.event_speakers.speaker_id IS
'Reference to the speaker user account.';

COMMENT ON COLUMN event.event_speakers.is_keynote IS
'True if the speaker is a keynote/main speaker.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
