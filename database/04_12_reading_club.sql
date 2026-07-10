/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_12_reading_club.sql
Version : 1.0
==============================================================
Description:
Reading Club Module
==============================================================
*/

\connect hssa_db;

SET search_path TO lms, public;

------------------------------------------------------------
-- BOOK CATEGORIES
------------------------------------------------------------

CREATE TABLE lms.book_categories
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    parent_id UUID,

    name VARCHAR(150) NOT NULL,

    description TEXT,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_book_category_parent
        FOREIGN KEY(parent_id)
        REFERENCES lms.book_categories(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- BOOKS
------------------------------------------------------------

CREATE TABLE lms.books
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    category_id UUID,

    title VARCHAR(255) NOT NULL,

    author VARCHAR(255),

    isbn VARCHAR(30),

    publisher VARCHAR(255),

    publication_year INTEGER,

    language VARCHAR(50),

    cover_media_id UUID,

    description TEXT,

    total_pages INTEGER,

    pdf_media_id UUID,

    external_link TEXT,

    is_featured BOOLEAN DEFAULT FALSE,

    is_active BOOLEAN DEFAULT TRUE,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_books_category
        FOREIGN KEY(category_id)
        REFERENCES lms.book_categories(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- READING LISTS
------------------------------------------------------------

CREATE TABLE lms.reading_lists
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id UUID NOT NULL,

    title VARCHAR(255) NOT NULL,

    description TEXT,

    is_public BOOLEAN DEFAULT FALSE,

    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_reading_list_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE
);

------------------------------------------------------------
-- READING LIST ITEMS
------------------------------------------------------------

CREATE TABLE lms.reading_list_items
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    reading_list_id UUID NOT NULL,

    book_id UUID NOT NULL,

    display_order INTEGER DEFAULT 0,

    added_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_reading_list
        FOREIGN KEY(reading_list_id)
        REFERENCES lms.reading_lists(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_reading_book
        FOREIGN KEY(book_id)
        REFERENCES lms.books(id)
        ON DELETE CASCADE,

    CONSTRAINT uq_reading_book
        UNIQUE(reading_list_id, book_id)
);

------------------------------------------------------------
-- BOOK REVIEWS
------------------------------------------------------------

CREATE TABLE lms.book_reviews
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    book_id UUID NOT NULL,

    user_id UUID NOT NULL,

    rating INTEGER NOT NULL,

    review TEXT,

    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_review_book
        FOREIGN KEY(book_id)
        REFERENCES lms.books(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_review_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE,

    CONSTRAINT chk_book_rating
        CHECK(rating BETWEEN 1 AND 5)
);

------------------------------------------------------------
-- BOOKMARKS
------------------------------------------------------------

CREATE TABLE lms.book_bookmarks
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id UUID NOT NULL,

    book_id UUID NOT NULL,

    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_bookmark_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_bookmark_book
        FOREIGN KEY(book_id)
        REFERENCES lms.books(id)
        ON DELETE CASCADE,

    CONSTRAINT uq_bookmark
        UNIQUE(user_id, book_id)
);

------------------------------------------------------------
-- INDEXES
------------------------------------------------------------

CREATE INDEX idx_books_category
ON lms.books(category_id);

CREATE INDEX idx_books_title
ON lms.books(title);

CREATE INDEX idx_reviews_book
ON lms.book_reviews(book_id);

CREATE INDEX idx_reviews_user
ON lms.book_reviews(user_id);

CREATE INDEX idx_reading_list_user
ON lms.reading_lists(user_id);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE lms.books IS
'Books available in the Reading Club.';

COMMENT ON TABLE lms.reading_lists IS
'User-created reading lists.';

COMMENT ON TABLE lms.book_reviews IS
'Book ratings and reviews by users.';

COMMENT ON TABLE lms.book_bookmarks IS
'Users bookmarked books.';