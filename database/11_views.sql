/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 11_views.sql
Version : 1.0
==============================================================
Description:
Database Views
==============================================================
*/

\connect hssa_db;

------------------------------------------------------------
-- USER PROFILE VIEW
------------------------------------------------------------

CREATE OR REPLACE VIEW public.v_user_profiles AS

SELECT

u.id,

u.email,

u.first_name,

u.last_name,

u.display_name,

u.account_status,

u.profile_image_media_id,

u.phone_number,

u.created_at

FROM auth.users u;

------------------------------------------------------------
-- COURSE DETAILS
------------------------------------------------------------

CREATE OR REPLACE VIEW lms.v_course_details AS

SELECT

c.id,

c.title,

c.slug,

cat.name AS category,

c.level,

c.status,

c.price,

c.is_free,

c.created_at

FROM lms.courses c

LEFT JOIN lms.course_categories cat

ON c.category_id=cat.id

;

------------------------------------------------------------
-- ENROLLMENT SUMMARY
------------------------------------------------------------

CREATE OR REPLACE VIEW lms.v_enrollment_summary AS

SELECT

e.course_id,

c.title,

COUNT(e.id) AS total_students

FROM lms.enrollments e

JOIN lms.courses c

ON e.course_id=c.id

GROUP BY

e.course_id,

c.title;

------------------------------------------------------------
-- PAYMENT SUMMARY
------------------------------------------------------------

CREATE OR REPLACE VIEW payment.v_payment_summary AS

SELECT

user_id,

COUNT(id) total_payments,

SUM(amount) total_amount

FROM payment.payments

GROUP BY user_id;

------------------------------------------------------------
-- RESEARCH SUMMARY
------------------------------------------------------------

CREATE OR REPLACE VIEW research.v_research_summary AS

SELECT

created_by,

COUNT(id) total_projects

FROM research.research_projects

GROUP BY created_by;

------------------------------------------------------------
-- PUBLICATION SUMMARY
------------------------------------------------------------

CREATE OR REPLACE VIEW publication.v_publication_summary AS

SELECT

created_by,

COUNT(id) total_publications

FROM publication.publications

GROUP BY created_by;

------------------------------------------------------------
-- NEWS VIEW
------------------------------------------------------------

CREATE OR REPLACE VIEW cms.v_latest_news AS

SELECT

id,

title,

slug,

published_at

FROM cms.news

WHERE status='PUBLISHED'

ORDER BY published_at DESC;

------------------------------------------------------------
-- EVENT VIEW
------------------------------------------------------------

CREATE OR REPLACE VIEW event.v_upcoming_events AS

SELECT

id,

title,

start_time,

end_time

FROM event.events

WHERE status='UPCOMING'

ORDER BY start_time;

------------------------------------------------------------
-- WORKSHOP VIEW
------------------------------------------------------------

CREATE OR REPLACE VIEW event.v_upcoming_workshops AS

SELECT

id,

title,

start_time,

end_time

FROM event.workshops

WHERE status='UPCOMING'

ORDER BY start_time;

------------------------------------------------------------
-- CERTIFICATE VIEW
------------------------------------------------------------

CREATE OR REPLACE VIEW lms.v_certificates AS

SELECT

c.id,

u.display_name,

co.title,

c.certificate_number,

c.issued_at

FROM lms.certificates c

JOIN auth.users u

ON c.user_id=u.id

JOIN lms.courses co

ON c.course_id=co.id;

------------------------------------------------------------
-- NOTIFICATIONS
------------------------------------------------------------

CREATE OR REPLACE VIEW notification.v_unread_notifications AS

SELECT

recipient_id,

COUNT(id) unread_count

FROM notification.notifications

WHERE status='UNREAD'

GROUP BY recipient_id;

------------------------------------------------------------
-- AUDIT
------------------------------------------------------------

CREATE OR REPLACE VIEW audit.v_recent_activity AS

SELECT

a.id,

u.display_name,

a.module_name,

a.action,

a.created_at

FROM audit.audit_logs a

LEFT JOIN auth.users u

ON a.user_id=u.id

ORDER BY a.created_at DESC;

------------------------------------------------------------
-- VERIFY
------------------------------------------------------------

SELECT
schemaname,
viewname
FROM pg_views
ORDER BY
schemaname,
viewname;

------------------------------------------------------------
-- END OF FILE