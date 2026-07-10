/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_03_courses/04_03_08_course_reviews.sql
Version : 1.0
==============================================================

Description:
Creates the course_reviews table to store student feedback
and ratings for courses.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_03_courses/04_03_02_courses.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS lms;

SET search_path TO lms, public;

------------------------------------------------------------
-- COURSE REVIEWS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS lms.course_reviews
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    course_id           UUID NOT NULL,
    user_id             UUID NOT NULL,

    rating              INTEGER NOT NULL,
    review_text         TEXT,

    is_approved         BOOLEAN NOT NULL DEFAULT TRUE,
    is_anonymous        BOOLEAN NOT NULL DEFAULT FALSE,

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
    CONSTRAINT uq_course_reviews_user
        UNIQUE(course_id, user_id),

    CONSTRAINT chk_course_reviews_rating
        CHECK (rating >= 1 AND rating <= 5),

    CONSTRAINT fk_course_reviews_course
        FOREIGN KEY(course_id)
        REFERENCES lms.courses(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_course_reviews_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_course_reviews_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_course_reviews_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE lms.course_reviews IS
'Stores user ratings and feedback for educational courses.';

COMMENT ON COLUMN lms.course_reviews.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN lms.course_reviews.course_id IS
'Reference to the reviewed course.';

COMMENT ON COLUMN lms.course_reviews.user_id IS
'Reference to the user who wrote the review.';

COMMENT ON COLUMN lms.course_reviews.rating IS
'Numeric rating from 1 to 5 stars.';

COMMENT ON COLUMN lms.course_reviews.is_approved IS
'True if the review is publicly visible (can be set to FALSE for moderation).';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
