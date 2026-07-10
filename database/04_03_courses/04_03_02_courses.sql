/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_03_courses/04_03_02_courses.sql
Version : 1.0
==============================================================

Description:
Creates the core courses table for the LMS module.

Dependencies:
- 03_types.sql
- 04_01_auth/04_01_auth_users.sql
- 04_17_media.sql
- 04_03_courses/04_03_01_course_categories.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS lms;

SET search_path TO lms, public;

------------------------------------------------------------
-- COURSES
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS lms.courses
(
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    category_id             UUID NOT NULL,

    title                   VARCHAR(255) NOT NULL,
    slug                    VARCHAR(255) NOT NULL,

    subtitle                VARCHAR(500),
    description             TEXT,

    language                VARCHAR(50) DEFAULT 'English',

    --------------------------------------------------------
    -- Enums
    --------------------------------------------------------
    level                   lms.course_level NOT NULL DEFAULT 'BEGINNER',
    status                  lms.course_status NOT NULL DEFAULT 'DRAFT',
    visibility              lms.course_visibility NOT NULL DEFAULT 'PUBLIC',

    --------------------------------------------------------
    -- Media References
    --------------------------------------------------------
    thumbnail_media_id      UUID,
    banner_media_id         UUID,
    promotional_video_id    UUID,

    --------------------------------------------------------
    -- Pricing
    --------------------------------------------------------
    is_free                 BOOLEAN NOT NULL DEFAULT FALSE,
    price                   NUMERIC(10, 2),
    currency                VARCHAR(3) DEFAULT 'LKR',
    discounted_price        NUMERIC(10, 2),

    --------------------------------------------------------
    -- Metadata
    --------------------------------------------------------
    duration_hours          NUMERIC(6, 2),
    certification_available BOOLEAN NOT NULL DEFAULT TRUE,

    --------------------------------------------------------
    --------------------------------------------------------
    -- Version
    --------------------------------------------------------
    version             INTEGER NOT NULL DEFAULT 0,

    -- Audit Fields
    --------------------------------------------------------
    published_at            TIMESTAMPTZ,
    created_at              TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at              TIMESTAMPTZ,

    --------------------------------------------------------
    -- SEO
    --------------------------------------------------------
    meta_title              VARCHAR(255),
    meta_description        TEXT,
    meta_keywords           TEXT,

    created_by              UUID,
    updated_by              UUID,

    --------------------------------------------------------
    -- Constraints
    --------------------------------------------------------
    CONSTRAINT uq_courses_slug
        UNIQUE(slug),

    CONSTRAINT fk_courses_category
        FOREIGN KEY(category_id)
        REFERENCES lms.course_categories(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_courses_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_courses_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_courses_price
        CHECK (price >= 0),

    CONSTRAINT chk_courses_discount
        CHECK (discounted_price >= 0 AND discounted_price <= price)
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE lms.courses IS
'Core table for storing educational courses.';

COMMENT ON COLUMN lms.courses.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN lms.courses.slug IS
'URL-friendly string for the course URL.';

COMMENT ON COLUMN lms.courses.thumbnail_media_id IS
'Reference to the course thumbnail image in the media library.';

COMMENT ON COLUMN lms.courses.banner_media_id IS
'Reference to the course banner image in the media library.';

COMMENT ON COLUMN lms.courses.promotional_video_id IS
'Reference to a promotional video file in the media library.';

COMMENT ON COLUMN lms.courses.is_free IS
'True if the course is free to enroll in.';

COMMENT ON COLUMN lms.courses.price IS
'Standard price of the course if not free.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
