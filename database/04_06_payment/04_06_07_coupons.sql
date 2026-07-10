/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_06_payment/04_06_07_coupons.sql
Version : 1.0
==============================================================

Description:
Creates the coupons table for global or generic promotional codes.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS payment;

SET search_path TO payment, public;

------------------------------------------------------------
-- COUPONS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS payment.coupons
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    code                VARCHAR(50) NOT NULL,

    discount_type       VARCHAR(20) NOT NULL, -- 'PERCENTAGE' or 'FIXED_AMOUNT'
    discount_value      NUMERIC(12, 2) NOT NULL,

    max_uses            INTEGER,
    used_count          INTEGER NOT NULL DEFAULT 0,

    expires_at          TIMESTAMPTZ,
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
    CONSTRAINT uq_coupons_code
        UNIQUE(code),

    CONSTRAINT fk_coupons_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_coupons_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_coupons_type
        CHECK (discount_type IN ('PERCENTAGE', 'FIXED_AMOUNT')),

    CONSTRAINT chk_coupons_value
        CHECK (discount_value > 0),

    CONSTRAINT chk_coupons_uses
        CHECK (used_count >= 0 AND (max_uses IS NULL OR used_count <= max_uses))
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE payment.coupons IS
'Stores promotional coupon codes applicable to purchases.';

COMMENT ON COLUMN payment.coupons.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN payment.coupons.code IS
'Unique promotional string code (e.g., HSSA2026).';

COMMENT ON COLUMN payment.coupons.discount_type IS
'Determines if the discount is a flat rate or percentage.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
