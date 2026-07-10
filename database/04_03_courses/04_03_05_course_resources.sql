/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_03_courses/04_03_05_course_resources.sql
Version : 1.0
==============================================================

Description:
Creates the course_resources table to manage supplementary 
materials attached directly to a course.

Dependencies:
- 03_types.sql
- 04_01_auth/04_01_auth_users.sql
- 04_17_media.sql
- 04_03_courses/04_03_02_courses.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS lms;

SET search_path TO lms, public;

------------------------------------------------------------
-- COURSE RESOURCES
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS lms.course_resources
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    course_id           UUID NOT NULL,

    title               VARCHAR(255) NOT NULL,
    description         TEXT,

    resource_type       lms.resource_type NOT NULL,

    --------------------------------------------------------
    -- File / Link Reference
    --------------------------------------------------------
    media_id            UUID,
    external_link       TEXT,

    is_downloadable     BOOLEAN NOT NULL DEFAULT TRUE,
    display_order       INTEGER NOT NULL DEFAULT 0,

    is_active           BOOLEAN NOT NULL DEFAULT TRUE,

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
    CONSTRAINT fk_course_resources_course
        FOREIGN KEY(course_id)
        REFERENCES lms.courses(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_course_resources_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_course_resources_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_resource_source
        CHECK (
            (media_id IS NOT NULL AND external_link IS NULL) OR
            (media_id IS NULL AND external_link IS NOT NULL)
        )
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE lms.course_resources IS
'Supplementary materials provided at the course level (e.g., syllabus, reading lists).';

COMMENT ON COLUMN lms.course_resources.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN lms.course_resources.course_id IS
'Reference to the associated course.';

COMMENT ON COLUMN lms.course_resources.resource_type IS
'Type of the resource (PDF, DOCUMENT, LINK, etc.).';

COMMENT ON COLUMN lms.course_resources.media_id IS
'Reference to the actual file in the media library if it is a hosted file.';

COMMENT ON COLUMN lms.course_resources.external_link IS
'External URL if the resource is a LINK.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
