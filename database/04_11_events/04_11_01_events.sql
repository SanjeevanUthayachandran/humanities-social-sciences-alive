/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_11_events/04_11_01_events.sql
Version : 1.0
==============================================================

Description:
Creates the events table to manage general live events, 
conferences, and seminars.

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
-- EVENTS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS event.events
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    title               VARCHAR(255) NOT NULL,
    slug                VARCHAR(255) NOT NULL,

    description         TEXT,

    event_type          event.event_type NOT NULL DEFAULT 'EVENT',
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
    CONSTRAINT uq_events_slug
        UNIQUE(slug),

    CONSTRAINT fk_events_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_events_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_events_time
        CHECK (start_time < end_time),

    CONSTRAINT chk_events_price
        CHECK (price IS NULL OR price >= 0)
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE event.events IS
'Core table for managing general live events, conferences, and seminars.';

COMMENT ON COLUMN event.events.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN event.events.event_type IS
'Classification (EVENT, SEMINAR, CONFERENCE, etc.).';

COMMENT ON COLUMN event.events.location_type IS
'ONLINE, PHYSICAL, or HYBRID mode.';

COMMENT ON COLUMN event.events.location_details IS
'Meeting URL for online events or physical address.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
