/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_06_payment/04_06_02_payment_transactions.sql
Version : 1.0
==============================================================

Description:
Creates the payment_transactions table for recording specific 
gateway attempts or bank transfer references.

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
-- PAYMENT TRANSACTIONS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS payment.payment_transactions
(
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    payment_id              UUID NOT NULL,

    transaction_reference   VARCHAR(255),
    gateway_response        JSONB,

    status              payment.payment_status NOT NULL DEFAULT 'PENDING',

    gateway_name            VARCHAR(100),
    gateway_transaction_id  VARCHAR(255),
    gateway_fee             NUMERIC(10, 2),
    exchange_rate           NUMERIC(10, 6),
    payment_reference       VARCHAR(255),

    --------------------------------------------------------
    --------------------------------------------------------
    -- Version
    --------------------------------------------------------
    version             INTEGER NOT NULL DEFAULT 0,

    -- Audit Fields
    --------------------------------------------------------
    created_at              TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at              TIMESTAMPTZ,
    created_by              UUID,
    updated_by              UUID,

    --------------------------------------------------------
    -- Constraints
    --------------------------------------------------------
    CONSTRAINT fk_payment_transactions_payment
        FOREIGN KEY(payment_id)
        REFERENCES payment.payments(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_payment_transactions_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_payment_transactions_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE payment.payment_transactions IS
'Records individual transaction attempts (e.g., PayHere webhook responses).';

COMMENT ON COLUMN payment.payment_transactions.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN payment.payment_transactions.payment_id IS
'Reference to the parent payment intent.';

COMMENT ON COLUMN payment.payment_transactions.transaction_reference IS
'External reference ID from the payment gateway (e.g., PayHere payment_id) or bank transfer ref.';

COMMENT ON COLUMN payment.payment_transactions.gateway_response IS
'Full JSON payload received from the gateway for auditing purposes.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
