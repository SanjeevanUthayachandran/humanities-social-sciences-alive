/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_06_payment/04_06_06_refunds.sql
Version : 1.0
==============================================================

Description:
Creates the refunds table for tracking refund requests and 
processed refunds.

Dependencies:
- 03_types.sql
- 04_01_auth/04_01_auth_users.sql
- 04_06_payment/04_06_01_payments.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS payment;

SET search_path TO payment, public;

------------------------------------------------------------
-- REFUNDS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS payment.refunds
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    payment_id          UUID NOT NULL,

    amount              NUMERIC(12, 2) NOT NULL,
    reason              TEXT NOT NULL,

    status              payment.payment_status NOT NULL DEFAULT 'PENDING',
    processed_at        TIMESTAMPTZ,

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
    CONSTRAINT fk_refunds_payment
        FOREIGN KEY(payment_id)
        REFERENCES payment.payments(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_refunds_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_refunds_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_refunds_amount
        CHECK (amount > 0)
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE payment.refunds IS
'Tracks refunds issued against a specific payment.';

COMMENT ON COLUMN payment.refunds.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN payment.refunds.payment_id IS
'Reference to the original payment.';

COMMENT ON COLUMN payment.refunds.amount IS
'The amount refunded (can be partial or full).';

COMMENT ON COLUMN payment.refunds.status IS
'Status of the refund (PENDING, SUCCESS, FAILED).';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
