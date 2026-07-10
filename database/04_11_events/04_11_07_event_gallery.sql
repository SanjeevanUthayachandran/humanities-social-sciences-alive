/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_11_events/04_11_07_event_gallery.sql
Version : 1.0
==============================================================

Description:
Creates the event_gallery table to attach images and videos 
showcasing a past event.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_11_events/04_11_01_events.sql
- 04_17_media.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS event;

SET search_path TO event, public;

------------------------------------------------------------
-- EVENT GALLERY
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS event.event_gallery
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    event_id            UUID NOT NULL,
    media_id            UUID NOT NULL,

    caption             VARCHAR(255),
    display_order       INTEGER NOT NULL DEFAULT 0,

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
    CONSTRAINT fk_eg_event
        FOREIGN KEY(event_id)
        REFERENCES event.events(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_eg_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_eg_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE event.event_gallery IS
'Photos and media specifically showcasing an event for public viewing.';

COMMENT ON COLUMN event.event_gallery.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN event.event_gallery.event_id IS
'Reference to the parent event.';

COMMENT ON COLUMN event.event_gallery.media_id IS
'Reference to the image/video file stored in the media library.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
