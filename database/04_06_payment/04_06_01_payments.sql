/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_06_payment/04_06_01_payments.sql
Version : 1.0
==============================================================

Description:
Creates the core payments table to track money collected.

Dependencies:
- 03_types.sql
- 04_01_auth/04_01_auth_users.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS payment;

SET search_path TO payment, public;

------------------------------------------------------------
-- PAYMENTS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS payment.payments
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id             UUID NOT NULL,

    amount              NUMERIC(12, 2) NOT NULL,
    currency            VARCHAR(3) NOT NULL DEFAULT 'LKR',

    payment_method      payment.payment_method NOT NULL,
    status              payment.payment_status NOT NULL DEFAULT 'PENDING',

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
    CONSTRAINT fk_payments_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_payments_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_payments_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_payments_amount
        CHECK (amount >= 0)
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE payment.payments IS
'Core table for recording payment intents and their high-level status.';

COMMENT ON COLUMN payment.payments.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN payment.payments.user_id IS
'Reference to the user making the payment.';

COMMENT ON COLUMN payment.payments.amount IS
'The total amount being paid.';

COMMENT ON COLUMN payment.payments.status IS
'Status of the payment (PENDING, SUCCESS, FAILED, REFUNDED, CANCELLED).';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
