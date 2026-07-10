/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_09_publications/04_09_08_publication_reviews.sql
Version : 1.0
==============================================================

Description:
Creates the publication_reviews table for storing feedback, 
ratings, and peer reviews for a publication.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_09_publications/04_09_02_publications.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS publication;

SET search_path TO publication, public;

------------------------------------------------------------
-- PUBLICATION REVIEWS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS publication.publication_reviews
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    publication_id      UUID NOT NULL,
    reviewer_id         UUID NOT NULL,

    rating              INTEGER NOT NULL,
    review_text         TEXT,

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
    CONSTRAINT uq_publication_reviews_user
        UNIQUE(publication_id, reviewer_id),

    CONSTRAINT chk_publication_reviews_rating
        CHECK (rating >= 1 AND rating <= 5),

    CONSTRAINT fk_publication_reviews_publication
        FOREIGN KEY(publication_id)
        REFERENCES publication.publications(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_publication_reviews_reviewer
        FOREIGN KEY(reviewer_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_publication_reviews_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_publication_reviews_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE publication.publication_reviews IS
'Stores feedback, peer reviews, or ratings for a publication.';

COMMENT ON COLUMN publication.publication_reviews.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN publication.publication_reviews.publication_id IS
'Reference to the publication being reviewed.';

COMMENT ON COLUMN publication.publication_reviews.reviewer_id IS
'Reference to the user/peer leaving the review.';

COMMENT ON COLUMN publication.publication_reviews.rating IS
'Numeric rating from 1 to 5 stars.';

COMMENT ON COLUMN publication.publication_reviews.is_approved IS
'True if the review is publicly visible.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
