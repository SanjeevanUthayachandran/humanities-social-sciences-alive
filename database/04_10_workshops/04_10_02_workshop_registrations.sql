/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_10_workshops/04_10_02_workshop_registrations.sql
Version : 1.0
==============================================================

Description:
Creates the workshop_registrations table to track users 
who sign up for an event.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_10_workshops/04_10_01_workshops.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS event;

SET search_path TO event, public;

------------------------------------------------------------
-- WORKSHOP REGISTRATIONS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS event.workshop_registrations
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    workshop_id         UUID NOT NULL,
    user_id             UUID NOT NULL,

    status              VARCHAR(50) NOT NULL DEFAULT 'REGISTERED', -- REGISTERED, CANCELLED, ATTENDED

    registered_at       TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

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
    CONSTRAINT uq_workshop_registrations
        UNIQUE(workshop_id, user_id),

    CONSTRAINT fk_wr_workshop
        FOREIGN KEY(workshop_id)
        REFERENCES event.workshops(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_wr_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_wr_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_wr_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE event.workshop_registrations IS
'Tracks users who have signed up/registered for a specific workshop.';

COMMENT ON COLUMN event.workshop_registrations.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN event.workshop_registrations.workshop_id IS
'Reference to the parent workshop.';

COMMENT ON COLUMN event.workshop_registrations.user_id IS
'Reference to the registered user.';

COMMENT ON COLUMN event.workshop_registrations.status IS
'Status of registration (e.g., REGISTERED, CANCELLED, WAITLISTED).';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
