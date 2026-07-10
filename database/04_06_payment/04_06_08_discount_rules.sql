/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_06_payment/04_06_08_discount_rules.sql
Version : 1.0
==============================================================

Description:
Creates the discount_rules table for applying automatic 
discounts to specific courses based on time periods.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_03_courses/04_03_02_courses.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS payment;

SET search_path TO payment, public;

------------------------------------------------------------
-- DISCOUNT RULES
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS payment.discount_rules
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    course_id           UUID NOT NULL,

    discount_percentage NUMERIC(5, 2) NOT NULL,

    valid_from          TIMESTAMPTZ,
    valid_until         TIMESTAMPTZ,

    is_active           BOOLEAN NOT NULL DEFAULT TRUE,

    --------------------------------------------------------
    --------------------------------------------------------
    -- Version
    --------------------------------------------------------
    version             INTEGER NOT NULL DEFAULT 0,

    -- Audit Fields
    --------------------------------------------------------
    created_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at          TIMESTAMPTZ,
    created_by          UUID,
    updated_by          UUID,

    --------------------------------------------------------
    -- Constraints
    --------------------------------------------------------
    CONSTRAINT fk_discount_rules_course
        FOREIGN KEY(course_id)
        REFERENCES lms.courses(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_discount_rules_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_discount_rules_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_discount_rules_percentage
        CHECK (discount_percentage > 0 AND discount_percentage <= 100),

    CONSTRAINT chk_discount_rules_dates
        CHECK (valid_from IS NULL OR valid_until IS NULL OR valid_from < valid_until)
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE payment.discount_rules IS
'Rules for automatically applying discounts to specific courses during a timeframe.';

COMMENT ON COLUMN payment.discount_rules.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN payment.discount_rules.course_id IS
'Reference to the course receiving the automated discount.';

COMMENT ON COLUMN payment.discount_rules.discount_percentage IS
'Percentage off the original price.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
