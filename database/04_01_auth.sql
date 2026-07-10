/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_01_auth_users.sql
Version : 1.0
Author  : Sanjeevan Uthayachandran
==============================================================

Description:
Creates the users table for authentication and user management.

Dependencies:
    01_database.sql
    02_extensions.sql
    03_types.sql
==============================================================
*/

\connect hssa_db;

SET search_path TO auth, public;

------------------------------------------------------------
-- USERS
------------------------------------------------------------

CREATE TABLE auth.users
(
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    --------------------------------------------------------
    -- Login
    --------------------------------------------------------
    email                   CITEXT NOT NULL,
    password_hash           TEXT,

    auth_provider           auth.auth_provider
                                NOT NULL
                                DEFAULT 'LOCAL',

    email_verified          BOOLEAN
                                NOT NULL
                                DEFAULT FALSE,

    email_verified_at       TIMESTAMPTZ,

    --------------------------------------------------------
    -- Basic Information
    --------------------------------------------------------
    first_name              VARCHAR(100) NOT NULL,

    middle_name             VARCHAR(100),

    last_name               VARCHAR(100) NOT NULL,

    display_name            VARCHAR(200),

    gender                  auth.gender,

    date_of_birth           DATE,

    profile_image_media_id  UUID,

    biography               TEXT,

    phone_number            VARCHAR(30),

    --------------------------------------------------------
    -- Address
    --------------------------------------------------------
    address_line_1          VARCHAR(255),

    address_line_2          VARCHAR(255),

    city                    VARCHAR(100),

    district                VARCHAR(100),

    province                VARCHAR(100),

    postal_code             VARCHAR(20),

    country                 VARCHAR(100)
                                DEFAULT 'Sri Lanka',

    --------------------------------------------------------
    -- Account
    --------------------------------------------------------
    

    account_status          auth.account_status
                                NOT NULL
                                DEFAULT 'PENDING',

    is_active               BOOLEAN
                                NOT NULL
                                DEFAULT TRUE,

    last_login_at           TIMESTAMPTZ,

    failed_login_attempts   INTEGER
                                NOT NULL
                                DEFAULT 0,

    locked_until            TIMESTAMPTZ,

    --------------------------------------------------------
    -- Instructor
    --------------------------------------------------------
    employee_number         VARCHAR(50),

    designation             VARCHAR(150),

    department              VARCHAR(150),

    qualification           VARCHAR(255),

    specialization          VARCHAR(255),

    office_location         VARCHAR(255),

    office_phone            VARCHAR(50),

    website                 TEXT,

    google_scholar          TEXT,

    researchgate            TEXT,

    orcid                   VARCHAR(100),

    linkedin                TEXT,

    --------------------------------------------------------
    -- Student
    --------------------------------------------------------
    student_number          VARCHAR(50),

    --------------------------------------------------------
    -- Audit
    --------------------------------------------------------
    created_at              TIMESTAMPTZ
                                NOT NULL
                                DEFAULT CURRENT_TIMESTAMP,

    updated_at              TIMESTAMPTZ
                                NOT NULL
                                DEFAULT CURRENT_TIMESTAMP,

    deleted_at              TIMESTAMPTZ,

    created_by              UUID,

    updated_by              UUID,

    --------------------------------------------------------
    -- Constraints
    --------------------------------------------------------
    CONSTRAINT uq_users_email
        UNIQUE(email),

    CONSTRAINT uq_employee_number
        UNIQUE(employee_number),

    CONSTRAINT uq_student_number
        UNIQUE(student_number),

    CONSTRAINT chk_failed_attempts
        CHECK (failed_login_attempts >= 0)
);

------------------------------------------------------------
-- SELF REFERENCING AUDIT
------------------------------------------------------------

ALTER TABLE auth.users
ADD CONSTRAINT fk_users_created_by
FOREIGN KEY (created_by)
REFERENCES auth.users(id)
ON DELETE SET NULL;

ALTER TABLE auth.users
ADD CONSTRAINT fk_users_updated_by
FOREIGN KEY (updated_by)
REFERENCES auth.users(id)
ON DELETE SET NULL;

------------------------------------------------------------
-- INDEXES
------------------------------------------------------------

CREATE INDEX idx_users_email
ON auth.users(email);

CREATE INDEX idx_users_status
ON auth.users(account_status);

CREATE INDEX idx_users_active
ON auth.users(is_active);

CREATE INDEX idx_users_last_login
ON auth.users(last_login_at);

CREATE INDEX idx_users_created_at
ON auth.users(created_at);

CREATE INDEX idx_users_first_name
ON auth.users(first_name);

CREATE INDEX idx_users_last_name
ON auth.users(last_name);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE auth.users IS
'Stores all platform users including administrators, instructors and students.';

COMMENT ON COLUMN auth.users.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN auth.users.email IS
'Unique login email address.';

COMMENT ON COLUMN auth.users.password_hash IS
'BCrypt password hash.';

COMMENT ON COLUMN auth.users.account_status IS
'Lifecycle status of user account.';

COMMENT ON COLUMN auth.users.email_verified IS
'True after email verification.';

COMMENT ON COLUMN auth.users.failed_login_attempts IS
'Number of failed login attempts.';

COMMENT ON COLUMN auth.users.locked_until IS
'Temporary account lock time.';

------------------------------------------------------------
-- SAMPLE ADMIN
------------------------------------------------------------

INSERT INTO auth.users
(
    email,
    password_hash,
    first_name,
    last_name,
    display_name,
    account_status,
    email_verified,
    auth_provider
)
VALUES
(
    'admin@hssa.lk',
    '$2a$12$ReplaceWithBCryptHash',
    'System',
    'Administrator',
    'Administrator',
    'ACTIVE',
    TRUE,
    'LOCAL'
);

------------------------------------------------------------
-- VERIFY
------------------------------------------------------------

SELECT
    id,
    email,
    account_status
FROM auth.users;

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------

/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_02_auth_roles.sql
Version : 1.0
Author  : Sanjeevan Uthayachandran
==============================================================

Description:
Creates the roles table and inserts the default system roles.

Dependencies:
    01_database.sql
    02_extensions.sql
    03_types.sql
    04_01_auth_users.sql
==============================================================
*/

\connect hssa_db;

SET search_path TO auth, public;

------------------------------------------------------------
-- ROLES
------------------------------------------------------------

CREATE TABLE auth.roles
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    role_code           VARCHAR(50)
                            NOT NULL,

    role_name           VARCHAR(100)
                            NOT NULL,

    description         TEXT,

    is_system           BOOLEAN
                            NOT NULL
                            DEFAULT TRUE,

    is_active           BOOLEAN
                            NOT NULL
                            DEFAULT TRUE,

    display_order       INTEGER
                            NOT NULL
                            DEFAULT 0,

    created_at          TIMESTAMPTZ
                            NOT NULL
                            DEFAULT CURRENT_TIMESTAMP,

    updated_at          TIMESTAMPTZ
                            NOT NULL
                            DEFAULT CURRENT_TIMESTAMP,

    deleted_at          TIMESTAMPTZ,

    created_by          UUID,

    updated_by          UUID,

    CONSTRAINT uq_roles_code
        UNIQUE(role_code),

    CONSTRAINT uq_roles_name
        UNIQUE(role_name)
);

------------------------------------------------------------
-- FOREIGN KEYS
------------------------------------------------------------

ALTER TABLE auth.roles
ADD CONSTRAINT fk_roles_created_by
FOREIGN KEY (created_by)
REFERENCES auth.users(id)
ON DELETE SET NULL;

ALTER TABLE auth.roles
ADD CONSTRAINT fk_roles_updated_by
FOREIGN KEY (updated_by)
REFERENCES auth.users(id)
ON DELETE SET NULL;

------------------------------------------------------------
-- INDEXES
------------------------------------------------------------

CREATE INDEX idx_roles_code
ON auth.roles(role_code);

CREATE INDEX idx_roles_name
ON auth.roles(role_name);

CREATE INDEX idx_roles_active
ON auth.roles(is_active);

CREATE INDEX idx_roles_system
ON auth.roles(is_system);

CREATE INDEX idx_roles_display_order
ON auth.roles(display_order);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE auth.roles IS
'Stores all system roles used for Role-Based Access Control (RBAC).';

COMMENT ON COLUMN auth.roles.role_code IS
'Unique role identifier used by Spring Security.';

COMMENT ON COLUMN auth.roles.role_name IS
'Human-readable role name.';

COMMENT ON COLUMN auth.roles.is_system IS
'True if this is a built-in role that cannot normally be deleted.';

------------------------------------------------------------
-- DEFAULT ROLES
------------------------------------------------------------

INSERT INTO auth.roles
(
    role_code,
    role_name,
    description,
    display_order
)
VALUES

(
    'ADMIN',
    'Administrator',
    'Complete access to the entire platform.',
    1
),

(
    'INSTRUCTOR',
    'Instructor',
    'Can manage own courses, research, publications and workshops.',
    2
),

(
    'USER',
    'Student',
    'Can enroll in courses, make payments and access learning resources.',
    3
);

------------------------------------------------------------
-- VERIFY
------------------------------------------------------------

SELECT
    role_code,
    role_name,
    is_active
FROM auth.roles
ORDER BY display_order;

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------

/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_03_auth_permissions.sql
Version : 1.0
Author  : Sanjeevan Uthayachandran
==============================================================

Description:
Creates system permissions used for RBAC.

Dependencies:
    01_database.sql
    02_extensions.sql
    03_types.sql
    04_01_auth_users.sql
    04_02_auth_roles.sql
==============================================================
*/

\connect hssa_db;

SET search_path TO auth, public;

------------------------------------------------------------
-- PERMISSIONS
------------------------------------------------------------

CREATE TABLE auth.permissions
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    permission_code     VARCHAR(100) NOT NULL,
    permission_name     VARCHAR(150) NOT NULL,

    module_name         VARCHAR(100) NOT NULL,

    description         TEXT,

    is_system           BOOLEAN NOT NULL DEFAULT TRUE,

    is_active           BOOLEAN NOT NULL DEFAULT TRUE,

    created_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    deleted_at          TIMESTAMPTZ,

    created_by          UUID,

    updated_by          UUID,

    CONSTRAINT uq_permission_code
        UNIQUE(permission_code)
);

------------------------------------------------------------
-- FOREIGN KEYS
------------------------------------------------------------

ALTER TABLE auth.permissions
ADD CONSTRAINT fk_permissions_created_by
FOREIGN KEY(created_by)
REFERENCES auth.users(id)
ON DELETE SET NULL;

ALTER TABLE auth.permissions
ADD CONSTRAINT fk_permissions_updated_by
FOREIGN KEY(updated_by)
REFERENCES auth.users(id)
ON DELETE SET NULL;

------------------------------------------------------------
-- INDEXES
------------------------------------------------------------

CREATE INDEX idx_permissions_code
ON auth.permissions(permission_code);

CREATE INDEX idx_permissions_module
ON auth.permissions(module_name);

CREATE INDEX idx_permissions_active
ON auth.permissions(is_active);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE auth.permissions IS
'Stores all permissions available in the RBAC system.';

COMMENT ON COLUMN auth.permissions.permission_code IS
'Unique permission identifier used by Spring Security.';

------------------------------------------------------------
-- AUTHENTICATION
------------------------------------------------------------

INSERT INTO auth.permissions
(permission_code,permission_name,module_name)
VALUES

('AUTH_LOGIN','User Login','Authentication'),
('AUTH_LOGOUT','User Logout','Authentication'),
('AUTH_REGISTER','Register User','Authentication'),
('AUTH_VERIFY_EMAIL','Verify Email','Authentication'),
('AUTH_RESET_PASSWORD','Reset Password','Authentication');

------------------------------------------------------------
-- USER MANAGEMENT
------------------------------------------------------------

INSERT INTO auth.permissions
(permission_code,permission_name,module_name)
VALUES

('USER_VIEW','View Users','Users'),
('USER_CREATE','Create User','Users'),
('USER_UPDATE','Update User','Users'),
('USER_DELETE','Delete User','Users'),
('USER_ASSIGN_ROLE','Assign User Role','Users');

------------------------------------------------------------
-- COURSE MANAGEMENT
------------------------------------------------------------

INSERT INTO auth.permissions
(permission_code,permission_name,module_name)
VALUES

('COURSE_VIEW','View Courses','Courses'),
('COURSE_CREATE','Create Course','Courses'),
('COURSE_UPDATE','Update Course','Courses'),
('COURSE_DELETE','Delete Course','Courses'),
('COURSE_PUBLISH','Publish Course','Courses');

------------------------------------------------------------
-- LESSONS
------------------------------------------------------------

INSERT INTO auth.permissions
(permission_code,permission_name,module_name)
VALUES

('LESSON_CREATE','Create Lesson','Lessons'),
('LESSON_UPDATE','Update Lesson','Lessons'),
('LESSON_DELETE','Delete Lesson','Lessons');

------------------------------------------------------------
-- ENROLLMENT
------------------------------------------------------------

INSERT INTO auth.permissions
(permission_code,permission_name,module_name)
VALUES

('ENROLL_VIEW','View Enrollments','Enrollment'),
('ENROLL_CREATE','Enroll Student','Enrollment'),
('ENROLL_CANCEL','Cancel Enrollment','Enrollment');

------------------------------------------------------------
-- PAYMENT
------------------------------------------------------------

INSERT INTO auth.permissions
(permission_code,permission_name,module_name)
VALUES

('PAYMENT_VIEW','View Payments','Payment'),
('PAYMENT_CREATE','Create Payment','Payment'),
('PAYMENT_VERIFY','Verify Payment','Payment'),
('PAYMENT_REFUND','Refund Payment','Payment');

------------------------------------------------------------
-- CERTIFICATES
------------------------------------------------------------

INSERT INTO auth.permissions
(permission_code,permission_name,module_name)
VALUES

('CERTIFICATE_VIEW','View Certificates','Certificates'),
('CERTIFICATE_GENERATE','Generate Certificate','Certificates'),
('CERTIFICATE_DOWNLOAD','Download Certificate','Certificates');

------------------------------------------------------------
-- RESEARCH
------------------------------------------------------------

INSERT INTO auth.permissions
(permission_code,permission_name,module_name)
VALUES

('RESEARCH_VIEW','View Research','Research'),
('RESEARCH_CREATE','Create Research','Research'),
('RESEARCH_UPDATE','Update Research','Research'),
('RESEARCH_DELETE','Delete Research','Research'),
('RESEARCH_PUBLISH','Publish Research','Research');

------------------------------------------------------------
-- PUBLICATIONS
------------------------------------------------------------

INSERT INTO auth.permissions
(permission_code,permission_name,module_name)
VALUES

('PUBLICATION_VIEW','View Publications','Publications'),
('PUBLICATION_CREATE','Create Publication','Publications'),
('PUBLICATION_UPDATE','Update Publication','Publications'),
('PUBLICATION_DELETE','Delete Publication','Publications');

------------------------------------------------------------
-- WORKSHOPS
------------------------------------------------------------

INSERT INTO auth.permissions
(permission_code,permission_name,module_name)
VALUES

('WORKSHOP_VIEW','View Workshops','Workshops'),
('WORKSHOP_CREATE','Create Workshop','Workshops'),
('WORKSHOP_UPDATE','Update Workshop','Workshops'),
('WORKSHOP_DELETE','Delete Workshop','Workshops');

------------------------------------------------------------
-- EVENTS
------------------------------------------------------------

INSERT INTO auth.permissions
(permission_code,permission_name,module_name)
VALUES

('EVENT_VIEW','View Events','Events'),
('EVENT_CREATE','Create Event','Events'),
('EVENT_UPDATE','Update Event','Events'),
('EVENT_DELETE','Delete Event','Events');

------------------------------------------------------------
-- READING CLUB
------------------------------------------------------------

INSERT INTO auth.permissions
(permission_code,permission_name,module_name)
VALUES

('READING_VIEW','View Reading Club','Reading Club'),
('READING_CREATE','Manage Reading Club','Reading Club');

------------------------------------------------------------
-- LITERARY CORNER
------------------------------------------------------------

INSERT INTO auth.permissions
(permission_code,permission_name,module_name)
VALUES

('LITERARY_VIEW','View Literary Corner','Literary'),
('LITERARY_CREATE','Create Literary Content','Literary'),
('LITERARY_UPDATE','Update Literary Content','Literary'),
('LITERARY_DELETE','Delete Literary Content','Literary');

------------------------------------------------------------
-- NEWS
------------------------------------------------------------

INSERT INTO auth.permissions
(permission_code,permission_name,module_name)
VALUES

('NEWS_VIEW','View News','News'),
('NEWS_CREATE','Create News','News'),
('NEWS_UPDATE','Update News','News'),
('NEWS_DELETE','Delete News','News');

------------------------------------------------------------
-- ANNOUNCEMENTS
------------------------------------------------------------

INSERT INTO auth.permissions
(permission_code,permission_name,module_name)
VALUES

('ANNOUNCEMENT_VIEW','View Announcements','Announcements'),
('ANNOUNCEMENT_CREATE','Create Announcement','Announcements'),
('ANNOUNCEMENT_UPDATE','Update Announcement','Announcements'),
('ANNOUNCEMENT_DELETE','Delete Announcement','Announcements');

------------------------------------------------------------
-- NOTIFICATIONS
------------------------------------------------------------

INSERT INTO auth.permissions
(permission_code,permission_name,module_name)
VALUES

('NOTIFICATION_VIEW','View Notifications','Notifications'),
('NOTIFICATION_SEND','Send Notifications','Notifications');

------------------------------------------------------------
-- CONTACT
------------------------------------------------------------

INSERT INTO auth.permissions
(permission_code,permission_name,module_name)
VALUES

('CONTACT_VIEW','View Contact Messages','Contact'),
('CONTACT_REPLY','Reply Contact Messages','Contact');

------------------------------------------------------------
-- MEDIA
------------------------------------------------------------

INSERT INTO auth.permissions
(permission_code,permission_name,module_name)
VALUES

('MEDIA_UPLOAD','Upload Media','Media'),
('MEDIA_DELETE','Delete Media','Media'),
('MEDIA_VIEW','View Media','Media');

------------------------------------------------------------
-- ADMIN
------------------------------------------------------------

INSERT INTO auth.permissions
(permission_code,permission_name,module_name)
VALUES

('SYSTEM_SETTINGS','Manage System Settings','Administration'),
('SYSTEM_BACKUP','Database Backup','Administration'),
('SYSTEM_AUDIT','View Audit Logs','Administration');

------------------------------------------------------------
-- VERIFY
------------------------------------------------------------

SELECT
    module_name,
    permission_code,
    permission_name
FROM auth.permissions
ORDER BY module_name, permission_code;

------------------------------------------------------------
-- END OF FILE

/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_04_auth_tokens.sql
Version : 1.0
Author  : Sanjeevan Uthayachandran
==============================================================

Description:
Creates authentication token tables.

Dependencies:
    01_database.sql
    02_extensions.sql
    03_types.sql
    04_01_auth_users.sql
==============================================================
*/

\connect hssa_db;

SET search_path TO auth, public;

------------------------------------------------------------
-- REFRESH TOKENS
------------------------------------------------------------

CREATE TABLE auth.refresh_tokens
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id             UUID NOT NULL,

    token               TEXT NOT NULL,

    device_name         VARCHAR(200),

    device_type         VARCHAR(100),

    operating_system    VARCHAR(100),

    browser             VARCHAR(100),

    ip_address          INET,

    user_agent          TEXT,

    expires_at          TIMESTAMPTZ NOT NULL,

    revoked_at          TIMESTAMPTZ,

    is_revoked          BOOLEAN NOT NULL DEFAULT FALSE,

    created_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_refresh_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE,

    CONSTRAINT uq_refresh_token
        UNIQUE(token)
);

------------------------------------------------------------
-- EMAIL VERIFICATION TOKENS
------------------------------------------------------------

CREATE TABLE auth.email_verification_tokens
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id             UUID NOT NULL,

    token               VARCHAR(255) NOT NULL,

    expires_at          TIMESTAMPTZ NOT NULL,

    verified_at         TIMESTAMPTZ,

    created_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_email_verification_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE,

    CONSTRAINT uq_email_verification_token
        UNIQUE(token)
);

------------------------------------------------------------
-- PASSWORD RESET TOKENS
------------------------------------------------------------

CREATE TABLE auth.password_reset_tokens
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id             UUID NOT NULL,

    token               VARCHAR(255) NOT NULL,

    expires_at          TIMESTAMPTZ NOT NULL,

    used_at             TIMESTAMPTZ,

    created_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_password_reset_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE,

    CONSTRAINT uq_password_reset_token
        UNIQUE(token)
);

------------------------------------------------------------
-- INDEXES
------------------------------------------------------------

CREATE INDEX idx_refresh_user
ON auth.refresh_tokens(user_id);

CREATE INDEX idx_refresh_expiry
ON auth.refresh_tokens(expires_at);

CREATE INDEX idx_refresh_revoked
ON auth.refresh_tokens(is_revoked);

CREATE INDEX idx_email_verification_user
ON auth.email_verification_tokens(user_id);

CREATE INDEX idx_email_verification_expiry
ON auth.email_verification_tokens(expires_at);

CREATE INDEX idx_password_reset_user
ON auth.password_reset_tokens(user_id);

CREATE INDEX idx_password_reset_expiry
ON auth.password_reset_tokens(expires_at);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE auth.refresh_tokens IS
'Stores JWT refresh tokens issued to authenticated users.';

COMMENT ON TABLE auth.email_verification_tokens IS
'Stores email verification tokens for newly registered users.';

COMMENT ON TABLE auth.password_reset_tokens IS
'Stores password reset tokens used during forgot password flow.';

COMMENT ON COLUMN auth.refresh_tokens.token IS
'Unique refresh token value.';

COMMENT ON COLUMN auth.email_verification_tokens.token IS
'Unique email verification token.';

COMMENT ON COLUMN auth.password_reset_tokens.token IS
'Unique password reset token.';

------------------------------------------------------------
-- VERIFY
------------------------------------------------------------

SELECT table_name
FROM information_schema.tables
WHERE table_schema='auth'
ORDER BY table_name;

------------------------------------------------------------
-- END OF FILE
/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_05_auth_sessions.sql
Version : 1.0
Author  : Sanjeevan Uthayachandran
==============================================================

Description:
Creates user session management and login history tables.

Dependencies:
    01_database.sql
    02_extensions.sql
    03_types.sql
    04_01_auth_users.sql
==============================================================
*/

\connect hssa_db;

SET search_path TO auth, public;

------------------------------------------------------------
-- USER SESSIONS
------------------------------------------------------------

CREATE TABLE auth.user_sessions
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id             UUID NOT NULL,

    refresh_token_id    UUID,

    session_token       VARCHAR(255) NOT NULL,

    ip_address          INET,

    user_agent          TEXT,

    browser             VARCHAR(100),

    browser_version     VARCHAR(100),

    operating_system    VARCHAR(100),

    device_type         VARCHAR(100),

    device_name         VARCHAR(200),

    login_at            TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    last_activity_at    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    expires_at          TIMESTAMPTZ NOT NULL,

    logout_at           TIMESTAMPTZ,

    is_active           BOOLEAN NOT NULL DEFAULT TRUE,

    created_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_sessions_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_sessions_refresh_token
        FOREIGN KEY(refresh_token_id)
        REFERENCES auth.refresh_tokens(id)
        ON DELETE SET NULL,

    CONSTRAINT uq_session_token
        UNIQUE(session_token)
);

------------------------------------------------------------
-- LOGIN HISTORY
------------------------------------------------------------

CREATE TABLE auth.login_history
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id             UUID,

    email               CITEXT,

    login_time          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    logout_time         TIMESTAMPTZ,

    ip_address          INET,

    user_agent          TEXT,

    browser             VARCHAR(100),

    browser_version     VARCHAR(100),

    operating_system    VARCHAR(100),

    device_type         VARCHAR(100),

    login_success       BOOLEAN NOT NULL,

    failure_reason      VARCHAR(255),

    country             VARCHAR(100),

    city                VARCHAR(100),

    latitude            NUMERIC(10,7),

    longitude           NUMERIC(10,7),

    created_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_login_history_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- INDEXES
------------------------------------------------------------

CREATE INDEX idx_sessions_user
ON auth.user_sessions(user_id);

CREATE INDEX idx_sessions_active
ON auth.user_sessions(is_active);

CREATE INDEX idx_sessions_login
ON auth.user_sessions(login_at);

CREATE INDEX idx_sessions_expiry
ON auth.user_sessions(expires_at);

CREATE INDEX idx_sessions_last_activity
ON auth.user_sessions(last_activity_at);

CREATE INDEX idx_login_history_user
ON auth.login_history(user_id);

CREATE INDEX idx_login_history_email
ON auth.login_history(email);

CREATE INDEX idx_login_history_time
ON auth.login_history(login_time);

CREATE INDEX idx_login_history_success
ON auth.login_history(login_success);

CREATE INDEX idx_login_history_ip
ON auth.login_history(ip_address);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE auth.user_sessions IS
'Stores all active user sessions across devices.';

COMMENT ON TABLE auth.login_history IS
'Stores historical login attempts for auditing and security.';

COMMENT ON COLUMN auth.user_sessions.session_token IS
'Unique identifier for the authenticated session.';

COMMENT ON COLUMN auth.user_sessions.last_activity_at IS
'Timestamp of the latest authenticated request.';

COMMENT ON COLUMN auth.login_history.login_success IS
'TRUE if login succeeded; FALSE otherwise.';

COMMENT ON COLUMN auth.login_history.failure_reason IS
'Reason for login failure if authentication failed.';

------------------------------------------------------------
-- VERIFY
------------------------------------------------------------

SELECT
    table_name
FROM
    information_schema.tables
WHERE
    table_schema = 'auth'
ORDER BY
    table_name;

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------

/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_07_auth_user_roles.sql
Version : 1.0
Author  : Sanjeevan Uthayachandran
==============================================================

Description:
Maps users to roles (Many-to-Many).

Dependencies:
    01_database.sql
    02_extensions.sql
    03_types.sql
    04_01_auth_users.sql
    04_02_auth_roles.sql
==============================================================
*/

\connect hssa_db;

SET search_path TO auth, public;

------------------------------------------------------------
-- USER ROLES
------------------------------------------------------------

CREATE TABLE auth.user_roles
(
    id                  UUID PRIMARY KEY
                            DEFAULT gen_random_uuid(),

    user_id             UUID NOT NULL,

    role_id             UUID NOT NULL,

    assigned_by         UUID,

    assigned_at         TIMESTAMPTZ
                            NOT NULL
                            DEFAULT CURRENT_TIMESTAMP,

    expires_at          TIMESTAMPTZ,

    is_active           BOOLEAN
                            NOT NULL
                            DEFAULT TRUE,

    notes               TEXT,

    created_at          TIMESTAMPTZ
                            NOT NULL
                            DEFAULT CURRENT_TIMESTAMP,

    updated_at          TIMESTAMPTZ
                            NOT NULL
                            DEFAULT CURRENT_TIMESTAMP,

    --------------------------------------------------------
    -- Foreign Keys
    --------------------------------------------------------

    CONSTRAINT fk_user_roles_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_user_roles_role
        FOREIGN KEY(role_id)
        REFERENCES auth.roles(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_user_roles_assigned_by
        FOREIGN KEY(assigned_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    --------------------------------------------------------
    -- Prevent Duplicate Role Assignment
    --------------------------------------------------------

    CONSTRAINT uq_user_role
        UNIQUE(user_id, role_id)
);

------------------------------------------------------------
-- INDEXES
------------------------------------------------------------

CREATE INDEX idx_user_roles_user
ON auth.user_roles(user_id);

CREATE INDEX idx_user_roles_role
ON auth.user_roles(role_id);

CREATE INDEX idx_user_roles_active
ON auth.user_roles(is_active);

CREATE INDEX idx_user_roles_assigned_at
ON auth.user_roles(assigned_at);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE auth.user_roles IS
'Maps users to one or more roles.';

COMMENT ON COLUMN auth.user_roles.assigned_by IS
'Administrator who assigned the role.';

COMMENT ON COLUMN auth.user_roles.expires_at IS
'Optional expiry date for temporary roles.';

COMMENT ON COLUMN auth.user_roles.notes IS
'Administrative notes regarding the role assignment.';



------------------------------------------------------------
-- VIEW
------------------------------------------------------------

CREATE OR REPLACE VIEW auth.v_user_roles AS
SELECT
    u.id,
    u.email,
    u.first_name,
    u.last_name,
    r.role_code,
    r.role_name,
    ur.assigned_at,
    ur.expires_at,
    ur.is_active
FROM auth.user_roles ur
JOIN auth.users u
    ON ur.user_id = u.id
JOIN auth.roles r
    ON ur.role_id = r.id
WHERE ur.is_active = TRUE;

------------------------------------------------------------
-- VERIFY
------------------------------------------------------------

SELECT *
FROM auth.v_user_roles
ORDER BY email;

------------------------------------------------------------
-- END OF FILE

/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_06_auth_role_permissions.sql
Version : 1.0
Author  : Sanjeevan Uthayachandran
==============================================================

Description:
Maps roles to permissions (RBAC).

Dependencies:
    01_database.sql
    02_extensions.sql
    03_types.sql
    04_01_auth_users.sql
    04_02_auth_roles.sql
    04_03_auth_permissions.sql
==============================================================
*/

\connect hssa_db;

SET search_path TO auth, public;

------------------------------------------------------------
-- ROLE PERMISSIONS
------------------------------------------------------------

CREATE TABLE auth.role_permissions
(
    id                  UUID PRIMARY KEY
                            DEFAULT gen_random_uuid(),

    role_id             UUID NOT NULL,

    permission_id       UUID NOT NULL,

    granted_by          UUID,

    granted_at          TIMESTAMPTZ
                            NOT NULL
                            DEFAULT CURRENT_TIMESTAMP,

    is_active           BOOLEAN
                            NOT NULL
                            DEFAULT TRUE,

    notes               TEXT,

    created_at          TIMESTAMPTZ
                            NOT NULL
                            DEFAULT CURRENT_TIMESTAMP,

    updated_at          TIMESTAMPTZ
                            NOT NULL
                            DEFAULT CURRENT_TIMESTAMP,

    --------------------------------------------------------
    -- Foreign Keys
    --------------------------------------------------------

    CONSTRAINT fk_role_permissions_role
        FOREIGN KEY(role_id)
        REFERENCES auth.roles(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_role_permissions_permission
        FOREIGN KEY(permission_id)
        REFERENCES auth.permissions(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_role_permissions_granted_by
        FOREIGN KEY(granted_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    --------------------------------------------------------
    -- Prevent duplicate permissions
    --------------------------------------------------------

    CONSTRAINT uq_role_permission
        UNIQUE(role_id, permission_id)
);

------------------------------------------------------------
-- INDEXES
------------------------------------------------------------

CREATE INDEX idx_role_permissions_role
ON auth.role_permissions(role_id);

CREATE INDEX idx_role_permissions_permission
ON auth.role_permissions(permission_id);

CREATE INDEX idx_role_permissions_active
ON auth.role_permissions(is_active);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE auth.role_permissions IS
'Maps permissions to system roles.';

COMMENT ON COLUMN auth.role_permissions.role_id IS
'Role receiving the permission.';

COMMENT ON COLUMN auth.role_permissions.permission_id IS
'Permission granted to the role.';

COMMENT ON COLUMN auth.role_permissions.granted_by IS
'Administrator who granted the permission.';

------------------------------------------------------------
-- ADMIN
-- Give ADMIN every permission
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
WHERE r.role_code='ADMIN';

------------------------------------------------------------
-- INSTRUCTOR
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
JOIN auth.permissions p
ON p.permission_code IN
(
'COURSE_VIEW',
'COURSE_CREATE',
'COURSE_UPDATE',
'COURSE_PUBLISH',

'LESSON_CREATE',
'LESSON_UPDATE',
'LESSON_DELETE',

'RESEARCH_VIEW',
'RESEARCH_CREATE',
'RESEARCH_UPDATE',
'RESEARCH_DELETE',
'RESEARCH_PUBLISH',

'PUBLICATION_VIEW',
'PUBLICATION_CREATE',
'PUBLICATION_UPDATE',
'PUBLICATION_DELETE',

'WORKSHOP_VIEW',
'WORKSHOP_CREATE',
'WORKSHOP_UPDATE',
'WORKSHOP_DELETE',

'EVENT_VIEW',
'EVENT_CREATE',
'EVENT_UPDATE',
'EVENT_DELETE',

'NEWS_VIEW',
'NEWS_CREATE',
'NEWS_UPDATE',
'NEWS_DELETE',

'ANNOUNCEMENT_VIEW',

'MEDIA_UPLOAD',
'MEDIA_VIEW',

'NOTIFICATION_VIEW',

'CERTIFICATE_VIEW'
)
WHERE r.role_code='INSTRUCTOR';

------------------------------------------------------------
-- USER
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
JOIN auth.permissions p
ON p.permission_code IN
(
'AUTH_LOGIN',
'AUTH_LOGOUT',
'AUTH_VERIFY_EMAIL',
'AUTH_RESET_PASSWORD',

'COURSE_VIEW',

'ENROLL_VIEW',
'ENROLL_CREATE',

'PAYMENT_CREATE',

'CERTIFICATE_VIEW',
'CERTIFICATE_DOWNLOAD',

'READING_VIEW',

'LITERARY_VIEW',

'NEWS_VIEW',

'ANNOUNCEMENT_VIEW',

'NOTIFICATION_VIEW',

'MEDIA_VIEW'
)
WHERE r.role_code='USER';

------------------------------------------------------------
-- VIEW
------------------------------------------------------------

CREATE OR REPLACE VIEW auth.v_role_permissions AS

SELECT

    r.role_code,

    r.role_name,

    p.permission_code,

    p.permission_name,

    p.module_name

FROM auth.role_permissions rp

JOIN auth.roles r

ON rp.role_id=r.id

JOIN auth.permissions p

ON rp.permission_id=p.id

WHERE rp.is_active=TRUE

ORDER BY
r.role_code,
p.module_name,
p.permission_code;

------------------------------------------------------------
-- VERIFY
------------------------------------------------------------

SELECT *
FROM auth.v_role_permissions;

------------------------------------------------------------
-- END OF FILE