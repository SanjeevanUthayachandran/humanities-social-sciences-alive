/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_11_events/04_11_03_event_sessions.sql
Version : 1.0
==============================================================

Description:
Creates the event_sessions table to divide large events 
into multiple time-bound sessions/talks.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_11_events/04_11_01_events.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS event;

SET search_path TO event, public;

------------------------------------------------------------
-- EVENT SESSIONS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS event.event_sessions
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    event_id            UUID NOT NULL,

    title               VARCHAR(255) NOT NULL,
    description         TEXT,

    start_time          TIMESTAMPTZ NOT NULL,
    end_time            TIMESTAMPTZ NOT NULL,

    speaker_id          UUID,

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
    CONSTRAINT fk_es_event
        FOREIGN KEY(event_id)
        REFERENCES event.events(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_es_speaker
        FOREIGN KEY(speaker_id)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_es_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_es_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_es_time
        CHECK (start_time < end_time)
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE event.event_sessions IS
'Sub-events or specific scheduled talks/sessions within a larger event.';

COMMENT ON COLUMN event.event_sessions.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN event.event_sessions.event_id IS
'Reference to the parent event.';

COMMENT ON COLUMN event.event_sessions.speaker_id IS
'Reference to the user leading this specific session (if applicable).';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
