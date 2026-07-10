/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_08_research/04_08_08_research_reviews.sql
Version : 1.0
==============================================================

Description:
Creates the research_reviews table for storing feedback, 
ratings, and peer reviews for a project.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_08_research/04_08_02_research_projects.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS research;

SET search_path TO research, public;

------------------------------------------------------------
-- RESEARCH REVIEWS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS research.research_reviews
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    project_id          UUID NOT NULL,
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
    CONSTRAINT uq_research_reviews_user
        UNIQUE(project_id, reviewer_id),

    CONSTRAINT chk_research_reviews_rating
        CHECK (rating >= 1 AND rating <= 5),

    CONSTRAINT fk_research_reviews_project
        FOREIGN KEY(project_id)
        REFERENCES research.research_projects(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_research_reviews_reviewer
        FOREIGN KEY(reviewer_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_research_reviews_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_research_reviews_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE research.research_reviews IS
'Stores feedback, peer reviews, or ratings for a research project.';

COMMENT ON COLUMN research.research_reviews.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN research.research_reviews.project_id IS
'Reference to the research project being reviewed.';

COMMENT ON COLUMN research.research_reviews.reviewer_id IS
'Reference to the user/peer leaving the review.';

COMMENT ON COLUMN research.research_reviews.rating IS
'Numeric rating from 1 to 5 stars.';

COMMENT ON COLUMN research.research_reviews.is_approved IS
'True if the review is publicly visible.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
