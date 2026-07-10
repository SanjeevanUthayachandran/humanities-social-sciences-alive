/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_16_contact.sql
Version : 1.0
==============================================================
Description:
Contact Management Module
==============================================================
*/

\connect hssa_db;

SET search_path TO cms, public;

------------------------------------------------------------
-- CONTACT MESSAGES
------------------------------------------------------------

CREATE TABLE cms.contact_messages
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    full_name              VARCHAR(200) NOT NULL,

    email                  CITEXT NOT NULL,

    phone_number           VARCHAR(30),

    subject                VARCHAR(255) NOT NULL,

    inquiry_type           VARCHAR(50) NOT NULL,

    message                TEXT NOT NULL,

    status              cms.message_status NOT NULL DEFAULT 'NEW',

    priority            VARCHAR(50) DEFAULT 'NORMAL',
    assigned_at         TIMESTAMPTZ,
    closed_at           TIMESTAMPTZ,
    resolved_by         UUID,

    assigned_to            UUID,

    replied_by             UUID,

    reply_message          TEXT,

    replied_at             TIMESTAMPTZ,

    ip_address             INET,

    user_agent             TEXT,

    created_at             TIMESTAMPTZ
                                NOT NULL
                                DEFAULT CURRENT_TIMESTAMP,

    updated_at             TIMESTAMPTZ
                                NOT NULL
                                DEFAULT CURRENT_TIMESTAMP,

    deleted_at             TIMESTAMPTZ,

    CONSTRAINT fk_contact_assigned
        FOREIGN KEY(assigned_to)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_contact_reply
        FOREIGN KEY(replied_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- NEWSLETTER SUBSCRIBERS
------------------------------------------------------------

CREATE TABLE cms.newsletter_subscribers
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    email CITEXT
            NOT NULL
            UNIQUE,

    full_name VARCHAR(200),

    is_active BOOLEAN
            NOT NULL
            DEFAULT TRUE,

    subscribed_at TIMESTAMPTZ
            NOT NULL
            DEFAULT CURRENT_TIMESTAMP,

    unsubscribed_at TIMESTAMPTZ,

    created_at TIMESTAMPTZ
            NOT NULL
            DEFAULT CURRENT_TIMESTAMP
);

------------------------------------------------------------
-- CONTACT FAQ
------------------------------------------------------------

CREATE TABLE cms.contact_faq
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    question VARCHAR(500)
            NOT NULL,

    answer TEXT
            NOT NULL,

    display_order INTEGER
            DEFAULT 0,

    is_active BOOLEAN
            DEFAULT TRUE,

    created_at TIMESTAMPTZ
            DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMPTZ
            DEFAULT CURRENT_TIMESTAMP
);

------------------------------------------------------------
-- CONTACT OFFICE
------------------------------------------------------------

CREATE TABLE cms.contact_offices
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    office_name VARCHAR(200)
            NOT NULL,

    address TEXT,

    phone VARCHAR(50),

    email CITEXT,

    opening_hours VARCHAR(255),

    google_map_url TEXT,

    latitude NUMERIC(10,7),

    longitude NUMERIC(10,7),

    is_head_office BOOLEAN
            DEFAULT FALSE,

    is_active BOOLEAN
            DEFAULT TRUE,

    created_at TIMESTAMPTZ
            DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMPTZ
            DEFAULT CURRENT_TIMESTAMP
);

------------------------------------------------------------
-- INDEXES
------------------------------------------------------------

CREATE INDEX idx_contact_status
ON cms.contact_messages(status);

CREATE INDEX idx_contact_email
ON cms.contact_messages(email);

CREATE INDEX idx_contact_created
ON cms.contact_messages(created_at);

CREATE INDEX idx_newsletter_email
ON cms.newsletter_subscribers(email);

CREATE INDEX idx_office_active
ON cms.contact_offices(is_active);

------------------------------------------------------------
-- DEFAULT FAQ
------------------------------------------------------------

INSERT INTO cms.contact_faq
(
question,
answer,
display_order
)
VALUES

(
'How do I register for a course?',
'Create an account, log in, and enroll in the course from the Courses page.',
1
),

(
'Are free courses available?',
'Yes. Both free and paid courses are available on the platform.',
2
),

(
'How can I contact support?',
'Use the Contact Us form or send an email to the official support address.',
3
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE cms.contact_messages IS
'Stores messages submitted through the Contact Us page.';

COMMENT ON TABLE cms.newsletter_subscribers IS
'Stores newsletter subscribers.';

COMMENT ON TABLE cms.contact_faq IS
'Frequently asked questions displayed on the Contact page.';

COMMENT ON TABLE cms.contact_offices IS
'Stores office contact information and locations.';

------------------------------------------------------------
-- VERIFY
------------------------------------------------------------

SELECT
table_name
FROM information_schema.tables
WHERE table_schema='cms'
ORDER BY table_name;

------------------------------------------------------------
-- END OF FILE