/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_06_payment/04_06_03_invoices.sql
Version : 1.0
==============================================================

Description:
Creates the invoices table for billing and receipts.

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
-- INVOICES
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS payment.invoices
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id             UUID NOT NULL,
    payment_id          UUID,

    invoice_number      VARCHAR(50) NOT NULL,

    status              payment.invoice_status NOT NULL DEFAULT 'UNPAID',

    total_amount        NUMERIC(12, 2) NOT NULL,
    due_date            TIMESTAMPTZ,

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
    CONSTRAINT uq_invoices_number
        UNIQUE(invoice_number),

    CONSTRAINT fk_invoices_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_invoices_payment
        FOREIGN KEY(payment_id)
        REFERENCES payment.payments(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_invoices_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_invoices_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_invoices_amount
        CHECK (total_amount >= 0)
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE payment.invoices IS
'Stores official invoices/receipts generated for users.';

COMMENT ON COLUMN payment.invoices.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN payment.invoices.user_id IS
'Reference to the user being billed.';

COMMENT ON COLUMN payment.invoices.payment_id IS
'Reference to the successful payment, if paid.';

COMMENT ON COLUMN payment.invoices.invoice_number IS
'Human-readable unique invoice sequential number.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
