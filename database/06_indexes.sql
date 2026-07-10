/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 06_indexes.sql
Version : 1.0
==============================================================
Description:
Creates indexes for all database tables.

Execute AFTER:
05_constraints.sql
==============================================================
*/

\connect hssa_db;

------------------------------------------------------------
-- AUTH
------------------------------------------------------------

CREATE INDEX IF NOT EXISTS idx_users_email
ON auth.users(email);


CREATE INDEX IF NOT EXISTS idx_users_status
ON auth.users(account_status);

CREATE INDEX IF NOT EXISTS idx_users_created
ON auth.users(created_at);

CREATE INDEX IF NOT EXISTS idx_roles_code
ON auth.roles(role_code);

CREATE INDEX IF NOT EXISTS idx_permissions_code
ON auth.permissions(permission_code);

CREATE INDEX IF NOT EXISTS idx_user_roles_user
ON auth.user_roles(user_id);

CREATE INDEX IF NOT EXISTS idx_user_roles_role
ON auth.user_roles(role_id);

CREATE INDEX IF NOT EXISTS idx_role_permissions_role
ON auth.role_permissions(role_id);

CREATE INDEX IF NOT EXISTS idx_role_permissions_permission
ON auth.role_permissions(permission_id);

CREATE INDEX IF NOT EXISTS idx_refresh_user
ON auth.refresh_tokens(user_id);

CREATE INDEX IF NOT EXISTS idx_refresh_expiry
ON auth.refresh_tokens(expires_at);

CREATE INDEX IF NOT EXISTS idx_sessions_user
ON auth.user_sessions(user_id);

CREATE INDEX IF NOT EXISTS idx_login_history_user
ON auth.login_history(user_id);

------------------------------------------------------------
-- USERS
------------------------------------------------------------

CREATE INDEX IF NOT EXISTS idx_profiles_user
ON profiles(user_id);

CREATE INDEX IF NOT EXISTS idx_preferences_user
ON user_preferences(user_id);

------------------------------------------------------------
-- LMS
------------------------------------------------------------

CREATE INDEX IF NOT EXISTS idx_course_category
ON lms.courses(category_id);

CREATE INDEX IF NOT EXISTS idx_course_status
ON lms.courses(status);

CREATE INDEX IF NOT EXISTS idx_course_slug
ON lms.courses(slug);

CREATE INDEX IF NOT EXISTS idx_course_level
ON lms.courses(level);

CREATE INDEX IF NOT EXISTS idx_course_created
ON lms.courses(created_at);

CREATE INDEX IF NOT EXISTS idx_enrollment_user
ON lms.enrollments(user_id);

CREATE INDEX IF NOT EXISTS idx_enrollment_course
ON lms.enrollments(course_id);

------------------------------------------------------------
-- PAYMENT
------------------------------------------------------------

CREATE INDEX IF NOT EXISTS idx_payment_user
ON payment.payments(user_id);

CREATE INDEX IF NOT EXISTS idx_payment_status
ON payment.payments(status);

CREATE INDEX IF NOT EXISTS idx_payment_created
ON payment.payments(created_at);

------------------------------------------------------------
-- RESEARCH
------------------------------------------------------------

CREATE INDEX IF NOT EXISTS idx_research_author
ON research.research_projects(created_by);

CREATE INDEX IF NOT EXISTS idx_research_status
ON research.research_projects(status);

------------------------------------------------------------
-- PUBLICATIONS
------------------------------------------------------------

CREATE INDEX IF NOT EXISTS idx_publication_status
ON publication.publications(status);

CREATE INDEX IF NOT EXISTS idx_publication_type
ON publication.publications(publication_type);

------------------------------------------------------------
-- EVENTS
------------------------------------------------------------

CREATE INDEX IF NOT EXISTS idx_events_status
ON event.events(status);

CREATE INDEX IF NOT EXISTS idx_workshops_status
ON event.workshops(status);

------------------------------------------------------------
-- CMS
------------------------------------------------------------

CREATE INDEX IF NOT EXISTS idx_news_status
ON cms.news(status);

CREATE INDEX IF NOT EXISTS idx_news_slug
ON cms.news(slug);

CREATE INDEX IF NOT EXISTS idx_literary_status
ON cms.literary_articles(status);

CREATE INDEX IF NOT EXISTS idx_contact_status
ON cms.contact_messages(status);

------------------------------------------------------------
-- NOTIFICATIONS
------------------------------------------------------------

CREATE INDEX IF NOT EXISTS idx_notifications_user
ON notification.notifications(recipient_id);

CREATE INDEX IF NOT EXISTS idx_notifications_status
ON notification.notifications(status);

------------------------------------------------------------
-- MEDIA
------------------------------------------------------------

CREATE INDEX IF NOT EXISTS idx_media_type
ON media.media_files(media_type);

CREATE INDEX IF NOT EXISTS idx_media_uploaded
ON media.media_files(uploaded_by);

------------------------------------------------------------
-- AUDIT
------------------------------------------------------------

CREATE INDEX IF NOT EXISTS idx_audit_user
ON audit.audit_logs(user_id);

CREATE INDEX IF NOT EXISTS idx_audit_created
ON audit.audit_logs(created_at);

------------------------------------------------------------
-- END OF FILE