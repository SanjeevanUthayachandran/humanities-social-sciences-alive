/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_10_workshops/04_10_01_workshops.sql
Version : 1.0
==============================================================

Description:
Creates the workshops table to manage live events, seminars, 
and workshops.

Dependencies:
- 03_types.sql
- 04_01_auth/04_01_auth_users.sql
- 04_17_media.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS event;

SET search_path TO event, public;

------------------------------------------------------------
-- WORKSHOPS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS event.workshops
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    title               VARCHAR(255) NOT NULL,
    slug                VARCHAR(255) NOT NULL,

    description         TEXT,

    event_type          event.event_type NOT NULL DEFAULT 'WORKSHOP',
    status              event.event_status NOT NULL DEFAULT 'UPCOMING',

    cover_media_id      UUID,

    start_time          TIMESTAMPTZ NOT NULL,
    end_time            TIMESTAMPTZ NOT NULL,

    location_type       VARCHAR(50) NOT NULL DEFAULT 'ONLINE', -- ONLINE, PHYSICAL, HYBRID
    location_details    TEXT, -- URL or Address

    registration_deadline TIMESTAMPTZ,
    timezone            VARCHAR(100),
    meeting_link        TEXT,
    meeting_password    VARCHAR(255), -- URL or Address

    max_participants    INTEGER,
    is_free             BOOLEAN NOT NULL DEFAULT TRUE,
    price               NUMERIC(10, 2),

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
    CONSTRAINT uq_workshops_slug
        UNIQUE(slug),

    CONSTRAINT fk_workshops_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_workshops_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_workshops_time
        CHECK (start_time < end_time),

    CONSTRAINT chk_workshops_price
        CHECK (price IS NULL OR price >= 0)
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE event.workshops IS
'Core table for managing live events, workshops, and webinars.';

COMMENT ON COLUMN event.workshops.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN event.workshops.event_type IS
'Classification (EVENT, WORKSHOP, SEMINAR, etc.).';

COMMENT ON COLUMN event.workshops.location_type IS
'ONLINE, PHYSICAL, or HYBRID mode.';

COMMENT ON COLUMN event.workshops.location_details IS
'Meeting URL for online events or physical address.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
