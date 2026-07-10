/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_10_workshops/04_10_07_workshop_feedback.sql
Version : 1.0
==============================================================

Description:
Creates the workshop_feedback table for storing reviews 
and ratings from participants post-event.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_10_workshops/04_10_01_workshops.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS event;

SET search_path TO event, public;

------------------------------------------------------------
-- WORKSHOP FEEDBACK
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS event.workshop_feedback
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    workshop_id         UUID NOT NULL,
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
    CONSTRAINT uq_workshop_feedback_user
        UNIQUE(workshop_id, user_id),

    CONSTRAINT chk_workshop_feedback_rating
        CHECK (rating >= 1 AND rating <= 5),

    CONSTRAINT fk_wf_workshop
        FOREIGN KEY(workshop_id)
        REFERENCES event.workshops(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_wf_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_wf_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_wf_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE event.workshop_feedback IS
'Stores feedback and ratings provided by attendees of a workshop.';

COMMENT ON COLUMN event.workshop_feedback.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN event.workshop_feedback.workshop_id IS
'Reference to the workshop being evaluated.';

COMMENT ON COLUMN event.workshop_feedback.user_id IS
'Reference to the attendee leaving the feedback.';

COMMENT ON COLUMN event.workshop_feedback.rating IS
'Numeric rating from 1 to 5 stars.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
