/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 07_functions.sql
Version : 1.0
==============================================================
Description:
Common reusable PostgreSQL functions.
==============================================================
*/

\connect hssa_db;

------------------------------------------------------------
-- COMMON SCHEMA
------------------------------------------------------------

CREATE SCHEMA IF NOT EXISTS common;

SET search_path TO common, public;

------------------------------------------------------------
-- AUTO UPDATE updated_at
------------------------------------------------------------

CREATE OR REPLACE FUNCTION common.set_updated_at()
RETURNS TRIGGER
LANGUAGE plpgsql
AS
$$
BEGIN
    NEW.updated_at := CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$;

------------------------------------------------------------
-- GENERATE SLUG
------------------------------------------------------------

CREATE OR REPLACE FUNCTION common.generate_slug(input_text TEXT)
RETURNS TEXT
LANGUAGE plpgsql
IMMUTABLE
AS
$$
BEGIN
    RETURN lower(
        regexp_replace(
            trim(input_text),
            '[^a-zA-Z0-9]+',
            '-',
            'g'
        )
    );
END;
$$;

------------------------------------------------------------
-- RANDOM UUID
------------------------------------------------------------

CREATE OR REPLACE FUNCTION common.generate_uuid()
RETURNS UUID
LANGUAGE SQL
AS
$$
SELECT gen_random_uuid();
$$;

------------------------------------------------------------
-- IS EMAIL VERIFIED
------------------------------------------------------------

CREATE OR REPLACE FUNCTION common.is_email_verified(user_uuid UUID)
RETURNS BOOLEAN
LANGUAGE plpgsql
AS
$$
DECLARE
    verified BOOLEAN;
BEGIN

    SELECT email_verified
    INTO verified
    FROM auth.users
    WHERE id=user_uuid;

    RETURN COALESCE(verified,FALSE);

END;
$$;

------------------------------------------------------------
-- FULL NAME
------------------------------------------------------------

CREATE OR REPLACE FUNCTION common.get_full_name(user_uuid UUID)
RETURNS TEXT
LANGUAGE plpgsql
AS
$$
DECLARE
    fullname TEXT;
BEGIN

SELECT
CONCAT(first_name,' ',last_name)

INTO fullname

FROM auth.users

WHERE id=user_uuid;

RETURN fullname;

END;
$$;

------------------------------------------------------------
-- USER ROLE
------------------------------------------------------------

CREATE OR REPLACE FUNCTION common.get_primary_role(user_uuid UUID)
RETURNS TEXT
LANGUAGE plpgsql
AS
$$
DECLARE
    role_name TEXT;
BEGIN

SELECT
r.role_code

INTO role_name

FROM auth.user_roles ur

JOIN auth.roles r

ON ur.role_id=r.id

WHERE ur.user_id=user_uuid

AND ur.is_active=TRUE

LIMIT 1;

RETURN role_name;

END;
$$;

------------------------------------------------------------
-- COURSE ENROLLMENT COUNT
------------------------------------------------------------

CREATE OR REPLACE FUNCTION common.course_enrollment_count(course_uuid UUID)
RETURNS INTEGER
LANGUAGE plpgsql
AS
$$
DECLARE
total_students INTEGER;
BEGIN

SELECT COUNT(*)

INTO total_students

FROM lms.enrollments

WHERE course_id=course_uuid;

RETURN total_students;

END;
$$;

------------------------------------------------------------
-- COURSE COMPLETION %
------------------------------------------------------------

CREATE OR REPLACE FUNCTION common.course_completion_percentage(course_uuid UUID)
RETURNS NUMERIC
LANGUAGE plpgsql
AS
$$
BEGIN

RETURN 0;

END;
$$;

------------------------------------------------------------
-- USER UNREAD NOTIFICATIONS
------------------------------------------------------------

CREATE OR REPLACE FUNCTION common.unread_notifications(user_uuid UUID)
RETURNS INTEGER
LANGUAGE plpgsql
AS
$$
DECLARE
total_notifications INTEGER;
BEGIN

SELECT COUNT(*)

INTO total_notifications

FROM notification.notifications

WHERE recipient_id=user_uuid

AND status='UNREAD';

RETURN total_notifications;

END;
$$;

------------------------------------------------------------
-- VERIFY
------------------------------------------------------------

SELECT
routine_name
FROM information_schema.routines
WHERE routine_schema='common'
ORDER BY routine_name;

------------------------------------------------------------
-- END OF FILE