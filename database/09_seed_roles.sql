/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 09_seed_roles.sql
Version : 1.0
==============================================================
Description:
Seeds default roles, permissions and role-permission mappings.
==============================================================
*/

\connect hssa_db;

SET search_path TO auth, public;

------------------------------------------------------------
-- ROLES
------------------------------------------------------------

INSERT INTO auth.roles
(
    role_code,
    role_name,
    description,
    is_system,
    is_active,
    display_order
)
VALUES
(
    'ADMIN',
    'Administrator',
    'Complete system administration privileges.',
    TRUE,
    TRUE,
    1
),
(
    'INSTRUCTOR',
    'Instructor',
    'Can create and manage educational content.',
    TRUE,
    TRUE,
    2
),
(
    'USER',
    'User',
    'Platform learner and community member.',
    TRUE,
    TRUE,
    3
)
ON CONFLICT (role_code) DO NOTHING;

------------------------------------------------------------
-- PERMISSIONS
------------------------------------------------------------

INSERT INTO auth.permissions
(permission_code,permission_name,module_name)

VALUES

('AUTH_LOGIN','Login','Authentication'),
('AUTH_LOGOUT','Logout','Authentication'),
('AUTH_REGISTER','Register','Authentication'),
('AUTH_VERIFY_EMAIL','Verify Email','Authentication'),

('USER_VIEW','View Users','Users'),
('USER_CREATE','Create User','Users'),
('USER_UPDATE','Update User','Users'),
('USER_DELETE','Delete User','Users'),
('USER_ASSIGN_ROLE','Assign Role','Users'),

('COURSE_VIEW','View Courses','Courses'),
('COURSE_CREATE','Create Course','Courses'),
('COURSE_UPDATE','Update Course','Courses'),
('COURSE_DELETE','Delete Course','Courses'),
('COURSE_PUBLISH','Publish Course','Courses'),

('LESSON_CREATE','Create Lesson','Lessons'),
('LESSON_UPDATE','Update Lesson','Lessons'),
('LESSON_DELETE','Delete Lesson','Lessons'),

('ENROLL_CREATE','Enroll','Enrollment'),
('ENROLL_VIEW','View Enrollment','Enrollment'),

('PAYMENT_CREATE','Create Payment','Payment'),
('PAYMENT_VIEW','View Payment','Payment'),
('PAYMENT_REFUND','Refund Payment','Payment'),

('CERTIFICATE_VIEW','View Certificate','Certificates'),
('CERTIFICATE_GENERATE','Generate Certificate','Certificates'),

('RESEARCH_VIEW','View Research','Research'),
('RESEARCH_CREATE','Create Research','Research'),
('RESEARCH_UPDATE','Update Research','Research'),
('RESEARCH_DELETE','Delete Research','Research'),

('PUBLICATION_VIEW','View Publications','Publications'),
('PUBLICATION_CREATE','Create Publication','Publications'),
('PUBLICATION_UPDATE','Update Publication','Publications'),

('WORKSHOP_VIEW','View Workshops','Workshops'),
('WORKSHOP_CREATE','Create Workshop','Workshops'),

('EVENT_VIEW','View Events','Events'),
('EVENT_CREATE','Create Event','Events'),

('NEWS_VIEW','View News','News'),
('NEWS_CREATE','Create News','News'),

('NOTIFICATION_VIEW','View Notifications','Notifications'),

('MEDIA_UPLOAD','Upload Media','Media'),
('MEDIA_VIEW','View Media','Media'),

('SYSTEM_SETTINGS','System Settings','Administration')

ON CONFLICT (permission_code) DO NOTHING;

------------------------------------------------------------
-- ADMIN -> ALL PERMISSIONS
------------------------------------------------------------

INSERT INTO auth.role_permissions
(
    role_id,
    permission_id
)
SELECT
    r.id,
    p.id
FROM auth.roles r
CROSS JOIN auth.permissions p
WHERE r.role_code='ADMIN'
ON CONFLICT (role_id,permission_id) DO NOTHING;

------------------------------------------------------------
-- INSTRUCTOR PERMISSIONS
------------------------------------------------------------

INSERT INTO auth.role_permissions
(role_id,permission_id)

SELECT
r.id,
p.id

FROM auth.roles r

JOIN auth.permissions p

ON p.permission_code IN
(
'COURSE_VIEW',
'COURSE_CREATE',
'COURSE_UPDATE',
'COURSE_PUBLISH',

'LESSON_CREATE',
'LESSON_UPDATE',

'RESEARCH_VIEW',
'RESEARCH_CREATE',
'RESEARCH_UPDATE',

'PUBLICATION_VIEW',
'PUBLICATION_CREATE',
'PUBLICATION_UPDATE',

'WORKSHOP_VIEW',
'WORKSHOP_CREATE',

'EVENT_VIEW',
'EVENT_CREATE',

'NEWS_VIEW',
'NEWS_CREATE',

'MEDIA_UPLOAD',
'MEDIA_VIEW',

'NOTIFICATION_VIEW'
)

WHERE r.role_code='INSTRUCTOR'

ON CONFLICT (role_id,permission_id) DO NOTHING;

------------------------------------------------------------
-- USER PERMISSIONS
------------------------------------------------------------

INSERT INTO auth.role_permissions
(role_id,permission_id)

SELECT
r.id,
p.id

FROM auth.roles r

JOIN auth.permissions p

ON p.permission_code IN
(
'AUTH_LOGIN',
'AUTH_LOGOUT',
'AUTH_VERIFY_EMAIL',

'COURSE_VIEW',

'ENROLL_CREATE',
'ENROLL_VIEW',

'PAYMENT_CREATE',

'CERTIFICATE_VIEW',

'NEWS_VIEW',

'EVENT_VIEW',

'WORKSHOP_VIEW',

'MEDIA_VIEW',

'NOTIFICATION_VIEW'
)

WHERE r.role_code='USER'

ON CONFLICT (role_id,permission_id) DO NOTHING;

------------------------------------------------------------
-- VERIFY
------------------------------------------------------------

SELECT
r.role_code,
COUNT(rp.permission_id) AS total_permissions
FROM auth.roles r
LEFT JOIN auth.role_permissions rp
ON r.id=rp.role_id
GROUP BY r.role_code
ORDER BY r.role_code;

------------------------------------------------------------
-- END OF FILE