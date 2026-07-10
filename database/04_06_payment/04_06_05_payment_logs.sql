/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_06_payment/04_06_05_payment_logs.sql
Version : 1.0
==============================================================

Description:
Creates the payment_logs table for strictly auditing all 
payment-related system events.

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_06_payment/04_06_01_payments.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS payment;

SET search_path TO payment, public;

------------------------------------------------------------
-- PAYMENT LOGS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS payment.payment_logs
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    payment_id          UUID NOT NULL,

    action_type         VARCHAR(100) NOT NULL,
    log_data            JSONB,

    --------------------------------------------------------
    -- Audit Fields
    --------------------------------------------------------
    created_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          UUID,

    --------------------------------------------------------
    -- Constraints
    --------------------------------------------------------
    CONSTRAINT fk_payment_logs_payment
        FOREIGN KEY(payment_id)
        REFERENCES payment.payments(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_payment_logs_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE payment.payment_logs IS
'Append-only log for tracing all payment state changes and webhooks.';

COMMENT ON COLUMN payment.payment_logs.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN payment.payment_logs.payment_id IS
'Reference to the payment being logged.';

COMMENT ON COLUMN payment.payment_logs.action_type IS
'The event action (e.g., INITIATED, WEBHOOK_RECEIVED, STATUS_CHANGED).';

COMMENT ON COLUMN payment.payment_logs.log_data IS
'Any associated JSON payload or context for the log entry.';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
