/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_03_courses/04_03_04_course_tags.sql
Version : 1.0
==============================================================

Description:
Creates the tables for managing course tags, allowing multiple 
tags to be associated with multiple courses (Many-to-Many).

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_03_courses/04_03_02_courses.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS lms;

SET search_path TO lms, public;

------------------------------------------------------------
-- COURSE TAGS (Lookup Table)
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS lms.course_tags
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    tag_name            VARCHAR(100) NOT NULL,

    --------------------------------------------------------
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
    CONSTRAINT uq_course_tags_name
        UNIQUE(tag_name),

    CONSTRAINT fk_course_tags_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_course_tags_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COURSE TAG MAP (Many-to-Many)
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS lms.course_tag_map
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    course_id           UUID NOT NULL,
    tag_id              UUID NOT NULL,

    --------------------------------------------------------
    -- Audit Fields
    --------------------------------------------------------
    created_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          UUID,

    --------------------------------------------------------
    -- Constraints
    --------------------------------------------------------
    CONSTRAINT uq_course_tag_map
        UNIQUE(course_id, tag_id),

    CONSTRAINT fk_course_tag_map_course
        FOREIGN KEY(course_id)
        REFERENCES lms.courses(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_course_tag_map_tag
        FOREIGN KEY(tag_id)
        REFERENCES lms.course_tags(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_course_tag_map_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE lms.course_tags IS
'Lookup table for searchable tags to classify courses.';

COMMENT ON COLUMN lms.course_tags.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN lms.course_tags.tag_name IS
'The name of the tag. Must be unique.';

COMMENT ON TABLE lms.course_tag_map IS
'Many-to-many mapping table linking courses and tags.';

COMMENT ON COLUMN lms.course_tag_map.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN lms.course_tag_map.course_id IS
'Reference to the course.';

COMMENT ON COLUMN lms.course_tag_map.tag_id IS
'Reference to the tag.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
