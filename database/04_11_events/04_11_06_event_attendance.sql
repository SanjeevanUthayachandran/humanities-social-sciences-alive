/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_11_events/04_11_06_event_attendance.sql
Version : 1.0
==============================================================

Description:
Creates the event_attendance table to track actual physical 
or virtual attendance of registered users.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_11_events/04_11_02_event_registrations.sql
- 04_11_events/04_11_03_event_sessions.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS event;

SET search_path TO event, public;

------------------------------------------------------------
-- EVENT ATTENDANCE
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS event.event_attendance
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    registration_id     UUID NOT NULL,
    session_id          UUID, -- Nullable if attending the entire event rather than specific session

    joined_at           TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    left_at             TIMESTAMPTZ,

    duration_minutes    INTEGER,

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
    CONSTRAINT fk_ea_registration
        FOREIGN KEY(registration_id)
        REFERENCES event.event_registrations(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_ea_session
        FOREIGN KEY(session_id)
        REFERENCES event.event_sessions(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_ea_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_ea_duration
        CHECK (duration_minutes IS NULL OR duration_minutes >= 0)
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE event.event_attendance IS
'Tracks actual attendance (joins/leaves) of registered users in events/sessions.';

COMMENT ON COLUMN event.event_attendance.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN event.event_attendance.registration_id IS
'Reference to the user registration record.';

COMMENT ON COLUMN event.event_attendance.session_id IS
'Optional reference to a specific session (for multi-session events).';

COMMENT ON COLUMN event.event_attendance.duration_minutes IS
'Total time spent attending, useful for analytics and certification eligibility.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
