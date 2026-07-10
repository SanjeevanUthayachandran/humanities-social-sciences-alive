/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 10_seed_admin.sql
Version : 1.0
==============================================================
Description:
Creates the initial system administrator.

Execute AFTER:
08_seed_roles.sql
==============================================================
*/

\connect hssa_db;

SET search_path TO auth, public;

------------------------------------------------------------
-- DEFAULT ADMIN USER
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
    email_verified_at,

    auth_provider,

    is_active
)

VALUES
(
    'admin@hssa.lk',

    '$2a$12$ReplaceWithRealBCryptPasswordHash',

    'System',

    'Administrator',

    'System Administrator',

    'ACTIVE',

    TRUE,

    CURRENT_TIMESTAMP,

    'LOCAL',

    TRUE
)

ON CONFLICT (email)
DO NOTHING;

------------------------------------------------------------
-- ASSIGN ADMIN ROLE
------------------------------------------------------------

INSERT INTO auth.user_roles
(
    user_id,
    role_id,
    assigned_at,
    is_active
)

SELECT

u.id,

r.id,

CURRENT_TIMESTAMP,

TRUE

FROM auth.users u

JOIN auth.roles r

ON r.role_code='ADMIN'

WHERE u.email='admin@hssa.lk'

ON CONFLICT (user_id,role_id)
DO NOTHING;

------------------------------------------------------------
-- VERIFY
------------------------------------------------------------

SELECT

u.email,

r.role_code

FROM auth.users u

JOIN auth.user_roles ur

ON ur.user_id=u.id

JOIN auth.roles r

ON ur.role_id=r.id

WHERE u.email='admin@hssa.lk';

------------------------------------------------------------
-- END OF FILE