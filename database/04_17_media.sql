/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_17_media.sql
Version : 1.0
==============================================================
Description:
Media Library Management
==============================================================
*/

\connect hssa_db;

SET search_path TO media, public;

------------------------------------------------------------
-- MEDIA FOLDERS
------------------------------------------------------------

CREATE TABLE media.media_folders
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    parent_id UUID,

    folder_name VARCHAR(200) NOT NULL,

    folder_path TEXT NOT NULL,

    description TEXT,

    created_by UUID,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_media_folder_parent
        FOREIGN KEY(parent_id)
        REFERENCES media.media_folders(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_media_folder_user
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- MEDIA FILES
------------------------------------------------------------

CREATE TABLE media.media_files
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    folder_id UUID,

    uploaded_by UUID NOT NULL,

    media_type media.media_type NOT NULL,

    storage_provider media.storage_provider
            NOT NULL
            DEFAULT 'AWS_S3',

    original_name VARCHAR(255) NOT NULL,

    stored_name VARCHAR(255) NOT NULL,

    file_extension VARCHAR(20),

    mime_type VARCHAR(100),

    file_size BIGINT,

    file_url TEXT NOT NULL,

    thumbnail_url TEXT,

    width INTEGER,

    height INTEGER,

    duration_seconds INTEGER,

    checksum VARCHAR(255),

    alt_text VARCHAR(255),

    caption TEXT,

    is_public BOOLEAN DEFAULT FALSE,

    download_count INTEGER DEFAULT 0,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    deleted_at TIMESTAMPTZ,

    CONSTRAINT fk_media_folder
        FOREIGN KEY(folder_id)
        REFERENCES media.media_folders(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_media_user
        FOREIGN KEY(uploaded_by)
        REFERENCES auth.users(id)
);

------------------------------------------------------------
-- MEDIA TAGS
------------------------------------------------------------

CREATE TABLE media.media_tags
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    tag_name VARCHAR(100) UNIQUE NOT NULL,

    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

------------------------------------------------------------
-- MEDIA TAG MAP
------------------------------------------------------------

CREATE TABLE media.media_tag_map
(
    media_id UUID NOT NULL,

    tag_id UUID NOT NULL,

    PRIMARY KEY(media_id,tag_id),

    FOREIGN KEY(media_id)
        REFERENCES media.media_files(id)
        ON DELETE CASCADE,

    FOREIGN KEY(tag_id)
        REFERENCES media.media_tags(id)
        ON DELETE CASCADE
);

------------------------------------------------------------
-- MEDIA DOWNLOADS
------------------------------------------------------------

CREATE TABLE media.media_downloads
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    media_id UUID NOT NULL,

    user_id UUID,

    downloaded_at TIMESTAMPTZ
            DEFAULT CURRENT_TIMESTAMP,

    ip_address INET,

    CONSTRAINT fk_download_media
        FOREIGN KEY(media_id)
        REFERENCES media.media_files(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_download_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- INDEXES
------------------------------------------------------------

CREATE INDEX idx_media_folder
ON media.media_files(folder_id);

CREATE INDEX idx_media_user
ON media.media_files(uploaded_by);

CREATE INDEX idx_media_type
ON media.media_files(media_type);

CREATE INDEX idx_media_provider
ON media.media_files(storage_provider);

CREATE INDEX idx_media_public
ON media.media_files(is_public);

CREATE INDEX idx_media_download
ON media.media_downloads(media_id);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE media.media_files IS
'Central media repository for all uploaded files.';

COMMENT ON TABLE media.media_folders IS
'Logical folder structure for media files.';

COMMENT ON TABLE media.media_downloads IS
'Tracks downloads of media files.';

COMMENT ON TABLE media.media_tags IS
'Tags used to classify media files.';

------------------------------------------------------------
-- VERIFY
------------------------------------------------------------

SELECT
table_name
FROM information_schema.tables
WHERE table_schema='media'
ORDER BY table_name;

------------------------------------------------------------
-- END OF FILE