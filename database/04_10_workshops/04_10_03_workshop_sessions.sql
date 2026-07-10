/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_10_workshops/04_10_03_workshop_sessions.sql
Version : 1.0
==============================================================

Description:
Creates the workshop_sessions table to divide large events 
into multiple time-bound sessions/talks.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_10_workshops/04_10_01_workshops.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS event;

SET search_path TO event, public;

------------------------------------------------------------
-- WORKSHOP SESSIONS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS event.workshop_sessions
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    workshop_id         UUID NOT NULL,

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
    CONSTRAINT fk_ws_workshop
        FOREIGN KEY(workshop_id)
        REFERENCES event.workshops(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_ws_speaker
        FOREIGN KEY(speaker_id)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_ws_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_ws_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_ws_time
        CHECK (start_time < end_time)
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE event.workshop_sessions IS
'Sub-events or specific scheduled talks/sessions within a larger workshop.';

COMMENT ON COLUMN event.workshop_sessions.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN event.workshop_sessions.workshop_id IS
'Reference to the parent workshop.';

COMMENT ON COLUMN event.workshop_sessions.speaker_id IS
'Reference to the user leading this specific session (if applicable).';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
