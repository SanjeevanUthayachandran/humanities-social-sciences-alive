/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 05_constraints.sql
Version : 1.0
Description:
Cross-module foreign key constraints.

Execute AFTER all table files.
==============================================================
*/

\connect hssa_db;

------------------------------------------------------------
-- CERTIFICATES
------------------------------------------------------------

ALTER TABLE lms.certificates
ADD CONSTRAINT fk_certificate_course
FOREIGN KEY (course_id)
REFERENCES lms.courses(id)
ON DELETE CASCADE;

ALTER TABLE lms.certificates
ADD CONSTRAINT fk_certificate_user
FOREIGN KEY (user_id)
REFERENCES auth.users(id)
ON DELETE CASCADE;

------------------------------------------------------------
-- ENROLLMENT
------------------------------------------------------------

ALTER TABLE lms.enrollments
ADD CONSTRAINT fk_enrollment_course
FOREIGN KEY (course_id)
REFERENCES lms.courses(id)
ON DELETE CASCADE;

ALTER TABLE lms.enrollments
ADD CONSTRAINT fk_enrollment_user
FOREIGN KEY (user_id)
REFERENCES auth.users(id)
ON DELETE CASCADE;

------------------------------------------------------------
-- PAYMENTS
------------------------------------------------------------

ALTER TABLE payment.payments
ADD CONSTRAINT fk_payment_user
FOREIGN KEY (user_id)
REFERENCES auth.users(id)
ON DELETE CASCADE;

------------------------------------------------------------
-- VERIFY
------------------------------------------------------------

SELECT
    conname,
    contype
FROM pg_constraint
ORDER BY conname;

------------------------------------------------------------
-- END OF FILE

---------------------------------------------------------------
-- Media References (Cross-Schema)
---------------------------------------------------------------
ALTER TABLE lms.course_categories ADD CONSTRAINT fk_course_categories_icon FOREIGN KEY(icon_media_id) REFERENCES media.media_files(id) ON DELETE SET NULL;
ALTER TABLE lms.courses ADD CONSTRAINT fk_courses_thumbnail FOREIGN KEY(thumbnail_media_id) REFERENCES media.media_files(id) ON DELETE SET NULL;
ALTER TABLE lms.course_resources ADD CONSTRAINT fk_course_resources_media FOREIGN KEY(media_id) REFERENCES media.media_files(id) ON DELETE SET NULL;
ALTER TABLE lms.lesson_resources ADD CONSTRAINT fk_lesson_resources_media FOREIGN KEY(media_id) REFERENCES media.media_files(id) ON DELETE SET NULL;
ALTER TABLE lms.assignments ADD CONSTRAINT fk_assignments_media FOREIGN KEY(instruction_media_id) REFERENCES media.media_files(id) ON DELETE SET NULL;
ALTER TABLE lms.certificates ADD CONSTRAINT fk_certificates_media FOREIGN KEY(media_id) REFERENCES media.media_files(id) ON DELETE SET NULL;
ALTER TABLE lms.certificate_verification ADD CONSTRAINT fk_cert_verification_qr FOREIGN KEY(qr_code_media_id) REFERENCES media.media_files(id) ON DELETE SET NULL;
ALTER TABLE research.research_projects ADD CONSTRAINT fk_research_projects_cover FOREIGN KEY(cover_media_id) REFERENCES media.media_files(id) ON DELETE SET NULL;
ALTER TABLE publication.publications ADD CONSTRAINT fk_publications_cover FOREIGN KEY(cover_media_id) REFERENCES media.media_files(id) ON DELETE SET NULL;
ALTER TABLE event.workshops ADD CONSTRAINT fk_workshops_cover FOREIGN KEY(cover_media_id) REFERENCES media.media_files(id) ON DELETE SET NULL;
ALTER TABLE event.workshop_certificates ADD CONSTRAINT fk_wc_media FOREIGN KEY(media_id) REFERENCES media.media_files(id) ON DELETE SET NULL;
ALTER TABLE event.events ADD CONSTRAINT fk_events_cover FOREIGN KEY(cover_media_id) REFERENCES media.media_files(id) ON DELETE SET NULL;
ALTER TABLE auth.users ADD CONSTRAINT fk_users_profile_media FOREIGN KEY(profile_image_media_id) REFERENCES media.media_files(id) ON DELETE SET NULL;
ALTER TABLE public.profiles ADD CONSTRAINT fk_profile_media FOREIGN KEY(profile_image_media_id) REFERENCES media.media_files(id) ON DELETE SET NULL;
ALTER TABLE lms.books ADD CONSTRAINT fk_book_cover FOREIGN KEY(cover_media_id) REFERENCES media.media_files(id) ON DELETE SET NULL;
ALTER TABLE lms.books ADD CONSTRAINT fk_book_pdf FOREIGN KEY(pdf_media_id) REFERENCES media.media_files(id) ON DELETE SET NULL;
ALTER TABLE notification.notifications ADD CONSTRAINT fk_notification_image FOREIGN KEY(image_media_id) REFERENCES media.media_files(id) ON DELETE SET NULL;


---------------------------------------------------------------
-- Remaining Media References
---------------------------------------------------------------
ALTER TABLE lms.courses ADD CONSTRAINT fk_courses_banner FOREIGN KEY(banner_media_id) REFERENCES media.media_files(id) ON DELETE SET NULL;
ALTER TABLE lms.lesson_videos ADD CONSTRAINT fk_lesson_videos_media FOREIGN KEY(media_id) REFERENCES media.media_files(id) ON DELETE CASCADE;
ALTER TABLE lms.certificate_templates ADD CONSTRAINT fk_cert_templates_media FOREIGN KEY(background_media_id) REFERENCES media.media_files(id) ON DELETE SET NULL;
ALTER TABLE research.research_files ADD CONSTRAINT fk_research_files_media FOREIGN KEY(media_id) REFERENCES media.media_files(id) ON DELETE CASCADE;
ALTER TABLE publication.publication_files ADD CONSTRAINT fk_publication_files_media FOREIGN KEY(media_id) REFERENCES media.media_files(id) ON DELETE CASCADE;
ALTER TABLE event.workshop_resources ADD CONSTRAINT fk_wres_media FOREIGN KEY(media_id) REFERENCES media.media_files(id) ON DELETE CASCADE;
ALTER TABLE event.event_resources ADD CONSTRAINT fk_eres_media FOREIGN KEY(media_id) REFERENCES media.media_files(id) ON DELETE CASCADE;
ALTER TABLE event.event_gallery ADD CONSTRAINT fk_eg_media FOREIGN KEY(media_id) REFERENCES media.media_files(id) ON DELETE CASCADE;
ALTER TABLE lms.courses ADD CONSTRAINT fk_courses_promo_video FOREIGN KEY(promotional_video_id) REFERENCES media.media_files(id) ON DELETE SET NULL;
