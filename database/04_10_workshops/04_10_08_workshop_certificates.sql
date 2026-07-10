/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_10_workshops/04_10_08_workshop_certificates.sql
Version : 1.0
==============================================================

Description:
Creates the workshop_certificates table for storing records 
of participation certificates issued to attendees.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_10_workshops/04_10_01_workshops.sql
- 04_17_media.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS event;

SET search_path TO event, public;

------------------------------------------------------------
-- WORKSHOP CERTIFICATES
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS event.workshop_certificates
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    workshop_id         UUID NOT NULL,
    user_id             UUID NOT NULL,

    certificate_number  VARCHAR(100) NOT NULL,
    media_id            UUID,

    issued_at           TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

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
    CONSTRAINT uq_wc_number
        UNIQUE(certificate_number),

    CONSTRAINT uq_wc_workshop_user
        UNIQUE(workshop_id, user_id),

    CONSTRAINT fk_wc_workshop
        FOREIGN KEY(workshop_id)
        REFERENCES event.workshops(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_wc_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_wc_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_wc_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE event.workshop_certificates IS
'Stores official participation certificates awarded to workshop attendees.';

COMMENT ON COLUMN event.workshop_certificates.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN event.workshop_certificates.workshop_id IS
'Reference to the associated workshop.';

COMMENT ON COLUMN event.workshop_certificates.user_id IS
'Reference to the attendee receiving the certificate.';

COMMENT ON COLUMN event.workshop_certificates.certificate_number IS
'Unique public-facing certificate identifier string.';

COMMENT ON COLUMN event.workshop_certificates.media_id IS
'Reference to the generated PDF file in the media library.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
