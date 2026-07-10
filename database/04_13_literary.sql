/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_13_literary.sql
Version : 1.0
==============================================================
Description:
Literary Corner Module
==============================================================
*/

\connect hssa_db;

SET search_path TO cms, public;

------------------------------------------------------------
-- LITERARY CATEGORIES
------------------------------------------------------------

CREATE TABLE cms.literary_categories
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    parent_id UUID,

    name VARCHAR(150) NOT NULL,

    slug VARCHAR(180) UNIQUE NOT NULL,

    description TEXT,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    display_order INTEGER DEFAULT 0,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_literary_category_parent
        FOREIGN KEY(parent_id)
        REFERENCES cms.literary_categories(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- LITERARY ARTICLES
------------------------------------------------------------

CREATE TABLE cms.literary_articles
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    category_id UUID NOT NULL,

    author_id UUID NOT NULL,

    article_type cms.article_type NOT NULL,

    status cms.content_status NOT NULL DEFAULT 'DRAFT',

    title VARCHAR(255) NOT NULL,

    slug VARCHAR(300) UNIQUE NOT NULL,

    summary VARCHAR(500),

    content TEXT NOT NULL,

    featured_image TEXT,

    language VARCHAR(20) DEFAULT 'EN',

    reading_time INTEGER,

    is_featured BOOLEAN DEFAULT FALSE,

    allow_comments BOOLEAN DEFAULT TRUE,

    view_count INTEGER DEFAULT 0,

    published_at TIMESTAMPTZ,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    deleted_at TIMESTAMPTZ,

    CONSTRAINT fk_literary_category
        FOREIGN KEY(category_id)
        REFERENCES cms.literary_categories(id),

    CONSTRAINT fk_literary_author
        FOREIGN KEY(author_id)
        REFERENCES auth.users(id)
);

------------------------------------------------------------
-- ARTICLE TAGS
------------------------------------------------------------

CREATE TABLE cms.literary_tags
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    name VARCHAR(100) UNIQUE NOT NULL,

    slug VARCHAR(150) UNIQUE NOT NULL,

    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

------------------------------------------------------------
-- ARTICLE TAG MAP
------------------------------------------------------------

CREATE TABLE cms.literary_article_tags
(
    article_id UUID NOT NULL,

    tag_id UUID NOT NULL,

    PRIMARY KEY(article_id, tag_id),

    FOREIGN KEY(article_id)
        REFERENCES cms.literary_articles(id)
        ON DELETE CASCADE,

    FOREIGN KEY(tag_id)
        REFERENCES cms.literary_tags(id)
        ON DELETE CASCADE
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

CREATE TABLE cms.literary_comments
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    article_id UUID NOT NULL,

    user_id UUID NOT NULL,

    parent_comment_id UUID,

    comment TEXT NOT NULL,

    is_approved BOOLEAN DEFAULT TRUE,

    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_comment_article
        FOREIGN KEY(article_id)
        REFERENCES cms.literary_articles(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_comment_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_parent_comment
        FOREIGN KEY(parent_comment_id)
        REFERENCES cms.literary_comments(id)
        ON DELETE CASCADE
);

------------------------------------------------------------
-- LIKES
------------------------------------------------------------

CREATE TABLE cms.literary_likes
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    article_id UUID NOT NULL,

    user_id UUID NOT NULL,

    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_like_article
        FOREIGN KEY(article_id)
        REFERENCES cms.literary_articles(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_like_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE,

    CONSTRAINT uq_article_like
        UNIQUE(article_id, user_id)
);

------------------------------------------------------------
-- BOOKMARKS
------------------------------------------------------------

CREATE TABLE cms.literary_bookmarks
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    article_id UUID NOT NULL,

    user_id UUID NOT NULL,

    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_bookmark_article
        FOREIGN KEY(article_id)
        REFERENCES cms.literary_articles(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_bookmark_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE,

    CONSTRAINT uq_article_bookmark
        UNIQUE(article_id, user_id)
);

------------------------------------------------------------
-- INDEXES
------------------------------------------------------------

CREATE INDEX idx_literary_category
ON cms.literary_articles(category_id);

CREATE INDEX idx_literary_author
ON cms.literary_articles(author_id);

CREATE INDEX idx_literary_status
ON cms.literary_articles(status);

CREATE INDEX idx_literary_slug
ON cms.literary_articles(slug);

CREATE INDEX idx_literary_comments_article
ON cms.literary_comments(article_id);

CREATE INDEX idx_literary_comments_user
ON cms.literary_comments(user_id);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE cms.literary_articles IS
'Stores literary works such as essays, poems, reviews, articles and short stories.';

COMMENT ON TABLE cms.literary_comments IS
'User comments on literary articles.';

COMMENT ON TABLE cms.literary_likes IS
'Likes for literary articles.';

COMMENT ON TABLE cms.literary_bookmarks IS
'Bookmarks for literary articles.';

COMMENT ON TABLE cms.literary_tags IS
'Tags used to classify literary content.';