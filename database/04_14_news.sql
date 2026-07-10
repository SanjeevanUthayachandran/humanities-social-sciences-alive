/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_14_news.sql
Version : 1.0
==============================================================
Description:
News & Announcements Module
==============================================================
*/

\connect hssa_db;

SET search_path TO cms, public;

------------------------------------------------------------
-- NEWS CATEGORIES
------------------------------------------------------------

CREATE TABLE cms.news_categories
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    parent_id UUID,

    name VARCHAR(150) NOT NULL,

    slug VARCHAR(180) UNIQUE NOT NULL,

    description TEXT,

    color VARCHAR(20),

    icon VARCHAR(100),

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    display_order INTEGER DEFAULT 0,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_news_category_parent
        FOREIGN KEY(parent_id)
        REFERENCES cms.news_categories(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- NEWS
------------------------------------------------------------

CREATE TABLE cms.news
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    category_id UUID,

    author_id UUID NOT NULL,

    title VARCHAR(255) NOT NULL,

    slug VARCHAR(300) UNIQUE NOT NULL,

    summary VARCHAR(500),

    content TEXT NOT NULL,

    featured_image TEXT,

    status cms.news_status
            NOT NULL
            DEFAULT 'DRAFT',

    is_featured BOOLEAN DEFAULT FALSE,

    allow_comments BOOLEAN DEFAULT TRUE,

    view_count INTEGER DEFAULT 0,

    published_at TIMESTAMPTZ,

    expires_at TIMESTAMPTZ,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    deleted_at TIMESTAMPTZ,

    CONSTRAINT fk_news_category
        FOREIGN KEY(category_id)
        REFERENCES cms.news_categories(id),

    CONSTRAINT fk_news_author
        FOREIGN KEY(author_id)
        REFERENCES auth.users(id)
);

------------------------------------------------------------
-- ANNOUNCEMENTS
------------------------------------------------------------

CREATE TABLE cms.announcements
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    author_id UUID NOT NULL,

    title VARCHAR(255) NOT NULL,

    content TEXT NOT NULL,

    priority VARCHAR(20) DEFAULT 'NORMAL',

    publish_date TIMESTAMPTZ,

    expiry_date TIMESTAMPTZ,

    is_public BOOLEAN DEFAULT TRUE,

    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    deleted_at TIMESTAMPTZ,

    CONSTRAINT fk_announcement_author
        FOREIGN KEY(author_id)
        REFERENCES auth.users(id)
);

------------------------------------------------------------
-- NEWS TAGS
------------------------------------------------------------

CREATE TABLE cms.news_tags
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    name VARCHAR(100) UNIQUE NOT NULL,

    slug VARCHAR(150) UNIQUE NOT NULL,

    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

------------------------------------------------------------
-- NEWS TAG MAP
------------------------------------------------------------

CREATE TABLE cms.news_tag_map
(
    news_id UUID NOT NULL,

    tag_id UUID NOT NULL,

    PRIMARY KEY(news_id, tag_id),

    FOREIGN KEY(news_id)
        REFERENCES cms.news(id)
        ON DELETE CASCADE,

    FOREIGN KEY(tag_id)
        REFERENCES cms.news_tags(id)
        ON DELETE CASCADE
);

------------------------------------------------------------
-- NEWS COMMENTS
------------------------------------------------------------

CREATE TABLE cms.news_comments
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    news_id UUID NOT NULL,

    user_id UUID NOT NULL,

    parent_comment_id UUID,

    comment TEXT NOT NULL,

    is_approved BOOLEAN DEFAULT TRUE,

    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_news_comment
        FOREIGN KEY(news_id)
        REFERENCES cms.news(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_news_comment_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_news_parent_comment
        FOREIGN KEY(parent_comment_id)
        REFERENCES cms.news_comments(id)
        ON DELETE CASCADE
);

------------------------------------------------------------
-- NEWS LIKES
------------------------------------------------------------

CREATE TABLE cms.news_likes
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    news_id UUID NOT NULL,

    user_id UUID NOT NULL,

    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_news_like
        FOREIGN KEY(news_id)
        REFERENCES cms.news(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_news_like_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE,

    CONSTRAINT uq_news_like
        UNIQUE(news_id,user_id)
);

------------------------------------------------------------
-- INDEXES
------------------------------------------------------------

CREATE INDEX idx_news_category
ON cms.news(category_id);

CREATE INDEX idx_news_author
ON cms.news(author_id);

CREATE INDEX idx_news_status
ON cms.news(status);

CREATE INDEX idx_news_slug
ON cms.news(slug);

CREATE INDEX idx_news_publish
ON cms.news(published_at);

CREATE INDEX idx_news_comments
ON cms.news_comments(news_id);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE cms.news IS
'Stores news articles published by HSSA.';

COMMENT ON TABLE cms.announcements IS
'Stores official announcements.';

COMMENT ON TABLE cms.news_categories IS
'News categories.';

COMMENT ON TABLE cms.news_comments IS
'Comments on news articles.';

COMMENT ON TABLE cms.news_likes IS
'Likes on news articles.';