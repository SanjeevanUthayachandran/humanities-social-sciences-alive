/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 08_triggers.sql
Version : 1.0
==============================================================
Description:
Database triggers for automatic operations.

Dependencies:
    01_database.sql
    02_extensions.sql
    03_types.sql
    04_tables
    11_functions.sql
==============================================================
*/

\connect hssa_db;

------------------------------------------------------------
-- AUTH
------------------------------------------------------------

CREATE TRIGGER trg_users_updated_at
BEFORE UPDATE
ON auth.users
FOR EACH ROW
EXECUTE FUNCTION common.set_updated_at();

CREATE TRIGGER trg_roles_updated_at
BEFORE UPDATE
ON auth.roles
FOR EACH ROW
EXECUTE FUNCTION common.set_updated_at();

CREATE TRIGGER trg_permissions_updated_at
BEFORE UPDATE
ON auth.permissions
FOR EACH ROW
EXECUTE FUNCTION common.set_updated_at();

------------------------------------------------------------
-- USER MANAGEMENT
------------------------------------------------------------

CREATE TRIGGER trg_profiles_updated_at
BEFORE UPDATE
ON public.profiles
FOR EACH ROW
EXECUTE FUNCTION common.set_updated_at();

------------------------------------------------------------
-- LMS
------------------------------------------------------------

CREATE TRIGGER trg_courses_updated_at
BEFORE UPDATE
ON lms.courses
FOR EACH ROW
EXECUTE FUNCTION common.set_updated_at();

CREATE TRIGGER trg_lessons_updated_at
BEFORE UPDATE
ON lms.lessons
FOR EACH ROW
EXECUTE FUNCTION common.set_updated_at();

CREATE TRIGGER trg_enrollments_updated_at
BEFORE UPDATE
ON lms.enrollments
FOR EACH ROW
EXECUTE FUNCTION common.set_updated_at();

------------------------------------------------------------
-- PAYMENT
------------------------------------------------------------

CREATE TRIGGER trg_payments_updated_at
BEFORE UPDATE
ON payment.payments
FOR EACH ROW
EXECUTE FUNCTION common.set_updated_at();

------------------------------------------------------------
-- RESEARCH
------------------------------------------------------------

CREATE TRIGGER trg_research_updated_at
BEFORE UPDATE
ON research.research_projects
FOR EACH ROW
EXECUTE FUNCTION common.set_updated_at();

------------------------------------------------------------
-- PUBLICATIONS
------------------------------------------------------------

CREATE TRIGGER trg_publications_updated_at
BEFORE UPDATE
ON publication.publications
FOR EACH ROW
EXECUTE FUNCTION common.set_updated_at();

------------------------------------------------------------
-- EVENTS
------------------------------------------------------------

CREATE TRIGGER trg_events_updated_at
BEFORE UPDATE
ON event.events
FOR EACH ROW
EXECUTE FUNCTION common.set_updated_at();

CREATE TRIGGER trg_workshops_updated_at
BEFORE UPDATE
ON event.workshops
FOR EACH ROW
EXECUTE FUNCTION common.set_updated_at();

------------------------------------------------------------
-- READING CLUB
------------------------------------------------------------

CREATE TRIGGER trg_books_updated_at
BEFORE UPDATE
ON lms.books
FOR EACH ROW
EXECUTE FUNCTION common.set_updated_at();

------------------------------------------------------------
-- LITERARY
------------------------------------------------------------

CREATE TRIGGER trg_literary_updated_at
BEFORE UPDATE
ON cms.literary_articles
FOR EACH ROW
EXECUTE FUNCTION common.set_updated_at();

------------------------------------------------------------
-- NEWS
------------------------------------------------------------

CREATE TRIGGER trg_news_updated_at
BEFORE UPDATE
ON cms.news
FOR EACH ROW
EXECUTE FUNCTION common.set_updated_at();

------------------------------------------------------------
-- NOTIFICATIONS
------------------------------------------------------------

CREATE TRIGGER trg_notifications_updated_at
BEFORE UPDATE
ON notification.notifications
FOR EACH ROW
EXECUTE FUNCTION common.set_updated_at();

------------------------------------------------------------
-- CONTACT
------------------------------------------------------------

CREATE TRIGGER trg_contact_updated_at
BEFORE UPDATE
ON cms.contact_messages
FOR EACH ROW
EXECUTE FUNCTION common.set_updated_at();

------------------------------------------------------------
-- MEDIA
------------------------------------------------------------

CREATE TRIGGER trg_media_updated_at
BEFORE UPDATE
ON media.media_files
FOR EACH ROW
EXECUTE FUNCTION common.set_updated_at();

------------------------------------------------------------
-- AUDIT
------------------------------------------------------------

CREATE TRIGGER trg_audit_settings_updated_at
BEFORE UPDATE
ON audit.audit_settings
FOR EACH ROW
EXECUTE FUNCTION common.set_updated_at();

------------------------------------------------------------
-- END OF FILE