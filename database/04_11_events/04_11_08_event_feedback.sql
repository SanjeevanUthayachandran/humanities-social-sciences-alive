/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_11_events/04_11_08_event_feedback.sql
Version : 1.0
==============================================================

Description:
Creates the event_feedback table for storing reviews 
and ratings from participants post-event.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_11_events/04_11_01_events.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS event;

SET search_path TO event, public;

------------------------------------------------------------
-- EVENT FEEDBACK
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS event.event_feedback
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    event_id            UUID NOT NULL,
    user_id             UUID NOT NULL,

    rating              INTEGER NOT NULL,
    feedback_text       TEXT,

    is_approved         BOOLEAN NOT NULL DEFAULT TRUE,

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
    CONSTRAINT uq_event_feedback_user
        UNIQUE(event_id, user_id),

    CONSTRAINT chk_event_feedback_rating
        CHECK (rating >= 1 AND rating <= 5),

    CONSTRAINT fk_ef_event
        FOREIGN KEY(event_id)
        REFERENCES event.events(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_ef_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_ef_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_ef_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE event.event_feedback IS
'Stores feedback and ratings provided by attendees of an event.';

COMMENT ON COLUMN event.event_feedback.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN event.event_feedback.event_id IS
'Reference to the event being evaluated.';

COMMENT ON COLUMN event.event_feedback.user_id IS
'Reference to the attendee leaving the feedback.';

COMMENT ON COLUMN event.event_feedback.rating IS
'Numeric rating from 1 to 5 stars.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
