/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_02_users.sql
Version : 1.0
==============================================================
*/

\connect hssa_db;

SET search_path TO public;

------------------------------------------------------------
-- USER PROFILES
------------------------------------------------------------

CREATE TABLE profiles
(
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id                 UUID NOT NULL UNIQUE,

    profile_image_media_id  UUID,

    title                   VARCHAR(100),

    biography               TEXT,

    nationality             VARCHAR(100),

    preferred_language      VARCHAR(50) DEFAULT 'English',

    timezone                VARCHAR(100) DEFAULT 'Asia/Colombo',

    website                 TEXT,

    linkedin                TEXT,

    github                  TEXT,

    google_scholar          TEXT,

    researchgate            TEXT,

    orcid                   VARCHAR(100),

    created_at              TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at              TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_profile_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE
);

------------------------------------------------------------
-- USER ADDRESSES
------------------------------------------------------------

CREATE TABLE user_addresses
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id             UUID NOT NULL,

    address_type        VARCHAR(50),

    address_line_1      VARCHAR(255),

    address_line_2      VARCHAR(255),

    city                VARCHAR(100),

    district            VARCHAR(100),

    province            VARCHAR(100),

    postal_code         VARCHAR(20),

    country             VARCHAR(100),

    is_primary          BOOLEAN DEFAULT FALSE,

    created_at          TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_address_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE
);

------------------------------------------------------------
-- SOCIAL LINKS
------------------------------------------------------------

CREATE TABLE social_links
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id             UUID NOT NULL,

    platform            VARCHAR(50),

    profile_url         TEXT,

    created_at          TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_social_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE
);

------------------------------------------------------------
-- USER PREFERENCES
------------------------------------------------------------

CREATE TABLE user_preferences
(
    id                          UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id                     UUID NOT NULL UNIQUE,

    theme                       VARCHAR(20) DEFAULT 'LIGHT',

    language                    VARCHAR(20) DEFAULT 'EN',

    email_notifications         BOOLEAN DEFAULT TRUE,

    sms_notifications           BOOLEAN DEFAULT FALSE,

    push_notifications          BOOLEAN DEFAULT TRUE,

    created_at                  TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    updated_at                  TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_preferences_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE
);

------------------------------------------------------------
-- BOOKMARKS
------------------------------------------------------------

CREATE TABLE bookmarks
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id             UUID NOT NULL,

    bookmark_type       VARCHAR(50),

    reference_id        UUID NOT NULL,

    created_at          TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_bookmark_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE
);

------------------------------------------------------------
-- INDEXES
------------------------------------------------------------

CREATE INDEX idx_profile_user
ON profiles(user_id);

CREATE INDEX idx_address_user
ON user_addresses(user_id);

CREATE INDEX idx_social_user
ON social_links(user_id);

CREATE INDEX idx_preferences_user
ON user_preferences(user_id);

CREATE INDEX idx_bookmark_user
ON bookmarks(user_id);

------------------------------------------------------------
-- END OF FILE