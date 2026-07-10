/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 03_types.sql
Version : 1.0
Author  : Sanjeevan Uthayachandran
==============================================================

Description:
Creates all ENUM types used throughout the HSSA platform.

Execute after:
01_database.sql
02_extensions.sql

==============================================================
*/

--------------------------------------------------------------
-- Connect Database
--------------------------------------------------------------
\connect hssa_db;

--------------------------------------------------------------
-- AUTH SCHEMA
--------------------------------------------------------------

CREATE TYPE auth.user_role AS ENUM (
    'ADMIN',
    'INSTRUCTOR',
    'USER'
);

CREATE TYPE auth.account_status AS ENUM (
    'PENDING',
    'ACTIVE',
    'SUSPENDED',
    'BLOCKED',
    'DELETED'
);

CREATE TYPE auth.gender AS ENUM (
    'MALE',
    'FEMALE',
    'OTHER',
    'PREFER_NOT_TO_SAY'
);

CREATE TYPE auth.auth_provider AS ENUM (
    'LOCAL',
    'GOOGLE'
);

--------------------------------------------------------------
-- LMS SCHEMA
--------------------------------------------------------------

CREATE TYPE lms.course_level AS ENUM (
    'BEGINNER',
    'INTERMEDIATE',
    'ADVANCED',
    'ALL_LEVELS'
);

CREATE TYPE lms.course_status AS ENUM (
    'DRAFT',
    'UNDER_REVIEW',
    'PUBLISHED',
    'ARCHIVED'
);

CREATE TYPE lms.course_visibility AS ENUM (
    'PUBLIC',
    'PRIVATE',
    'UNLISTED'
);

CREATE TYPE lms.lesson_type AS ENUM (
    'VIDEO',
    'DOCUMENT',
    'PDF',
    'QUIZ',
    'ASSIGNMENT',
    'LIVE_SESSION',
    'TEXT'
);

CREATE TYPE lms.resource_type AS ENUM (
    'PDF',
    'IMAGE',
    'ZIP',
    'DOCUMENT',
    'LINK',
    'VIDEO'
);

CREATE TYPE lms.enrollment_status AS ENUM (
    'PENDING',
    'ENROLLED',
    'COMPLETED',
    'CANCELLED',
    'EXPIRED'
);

CREATE TYPE lms.progress_status AS ENUM (
    'NOT_STARTED',
    'IN_PROGRESS',
    'COMPLETED'
);

--------------------------------------------------------------
-- PAYMENT SCHEMA
--------------------------------------------------------------

CREATE TYPE payment.payment_status AS ENUM (
    'PENDING',
    'SUCCESS',
    'FAILED',
    'REFUNDED',
    'CANCELLED'
);

CREATE TYPE payment.payment_method AS ENUM (
    'PAYHERE',
    'BANK_TRANSFER',
    'CASH',
    'FREE'
);

CREATE TYPE payment.invoice_status AS ENUM (
    'UNPAID',
    'PAID',
    'OVERDUE',
    'VOID'
);

--------------------------------------------------------------
-- CERTIFICATE
--------------------------------------------------------------

CREATE TYPE lms.certificate_status AS ENUM (
    'GENERATED',
    'ISSUED',
    'REVOKED'
);

--------------------------------------------------------------
-- RESEARCH
--------------------------------------------------------------

CREATE TYPE research.research_status AS ENUM (
    'DRAFT',
    'UNDER_REVIEW',
    'PUBLISHED',
    'ARCHIVED'
);

CREATE TYPE research.research_type AS ENUM (
    'PROJECT',
    'THESIS',
    'ARTICLE',
    'REPORT',
    'BOOK'
);

--------------------------------------------------------------
-- PUBLICATION
--------------------------------------------------------------

CREATE TYPE publication.publication_type AS ENUM (
    'JOURNAL',
    'CONFERENCE',
    'BOOK',
    'BOOK_CHAPTER',
    'WORKING_PAPER',
    'REPORT',
    'MAGAZINE'
);

CREATE TYPE publication.publication_status AS ENUM (
    'DRAFT',
    'PUBLISHED',
    'ARCHIVED'
);

--------------------------------------------------------------
-- EVENTS
--------------------------------------------------------------

CREATE TYPE event.event_status AS ENUM (
    'UPCOMING',
    'OPEN',
    'ONGOING',
    'COMPLETED',
    'CANCELLED'
);

CREATE TYPE event.event_type AS ENUM (
    'EVENT',
    'WORKSHOP',
    'SEMINAR',
    'WEBINAR',
    'CONFERENCE'
);

--------------------------------------------------------------
-- READING CLUB
--------------------------------------------------------------

CREATE TYPE cms.book_status AS ENUM (
    'AVAILABLE',
    'BORROWED',
    'ARCHIVED'
);

--------------------------------------------------------------
-- LITERARY CORNER
--------------------------------------------------------------

CREATE TYPE cms.article_type AS ENUM (
    'ESSAY',
    'POEM',
    'REVIEW',
    'ARTICLE',
    'SHORT_STORY'
);

CREATE TYPE cms.content_status AS ENUM (
    'DRAFT',
    'PUBLISHED',
    'ARCHIVED'
);

--------------------------------------------------------------
-- NEWS
--------------------------------------------------------------

CREATE TYPE cms.news_status AS ENUM (
    'DRAFT',
    'PUBLISHED',
    'ARCHIVED'
);

--------------------------------------------------------------
-- NOTIFICATION
--------------------------------------------------------------

CREATE TYPE notification.notification_type AS ENUM (
    'SYSTEM',
    'COURSE',
    'PAYMENT',
    'CERTIFICATE',
    'EVENT',
    'WORKSHOP',
    'NEWS',
    'ANNOUNCEMENT'
);

CREATE TYPE notification.notification_status AS ENUM (
    'UNREAD',
    'READ'
);

--------------------------------------------------------------
-- CONTACT
--------------------------------------------------------------

CREATE TYPE cms.message_status AS ENUM (
    'NEW',
    'IN_PROGRESS',
    'RESOLVED',
    'CLOSED'
);

--------------------------------------------------------------
-- MEDIA
--------------------------------------------------------------

CREATE TYPE media.media_type AS ENUM (
    'IMAGE',
    'VIDEO',
    'PDF',
    'DOCUMENT',
    'AUDIO',
    'ARCHIVE',
    'OTHER'
);

CREATE TYPE media.storage_provider AS ENUM (
    'LOCAL',
    'AWS_S3',
    'YOUTUBE',
    'VIMEO'
);

--------------------------------------------------------------
-- AUDIT
--------------------------------------------------------------

CREATE TYPE audit.audit_action AS ENUM (
    'CREATE',
    'UPDATE',
    'DELETE',
    'LOGIN',
    'LOGOUT',
    'DOWNLOAD',
    'UPLOAD',
    'PUBLISH',
    'APPROVE',
    'REJECT'
);

--------------------------------------------------------------
-- Verify Types
--------------------------------------------------------------

SELECT
    n.nspname AS schema_name,
    t.typname AS enum_name
FROM pg_type t
JOIN pg_namespace n
ON n.oid = t.typnamespace
WHERE t.typtype = 'e'
ORDER BY schema_name, enum_name;

--------------------------------------------------------------
-- End of File
--------------------------------------------------------------