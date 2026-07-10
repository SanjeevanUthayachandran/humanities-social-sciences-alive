/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_06_payment/04_06_04_invoice_items.sql
Version : 1.0
==============================================================

Description:
Creates the invoice_items table representing line items
on an invoice (e.g., Course A, Workshop B).

Dependencies:
- 04_01_auth/04_01_auth_users.sql
- 04_06_payment/04_06_03_invoices.sql
==============================================================
*/

\connect hssa_db;

CREATE SCHEMA IF NOT EXISTS payment;

SET search_path TO payment, public;

------------------------------------------------------------
-- INVOICE ITEMS
------------------------------------------------------------

CREATE TABLE IF NOT EXISTS payment.invoice_items
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    invoice_id          UUID NOT NULL,

    item_type           VARCHAR(50) NOT NULL,
    item_id             UUID NOT NULL,

    description         VARCHAR(255) NOT NULL,

    unit_price          NUMERIC(12, 2) NOT NULL,
    quantity            INTEGER NOT NULL DEFAULT 1,
    total_price         NUMERIC(12, 2) NOT NULL,

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
    CONSTRAINT fk_invoice_items_invoice
        FOREIGN KEY(invoice_id)
        REFERENCES payment.invoices(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_invoice_items_created
        FOREIGN KEY(created_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_invoice_items_updated
        FOREIGN KEY(updated_by)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_invoice_items_quantity
        CHECK (quantity > 0),

    CONSTRAINT chk_invoice_items_prices
        CHECK (unit_price >= 0 AND total_price >= 0)
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE payment.invoice_items IS
'Line items for a specific invoice.';

COMMENT ON COLUMN payment.invoice_items.id IS
'Primary UUID identifier.';

COMMENT ON COLUMN payment.invoice_items.invoice_id IS
'Reference to the parent invoice.';

COMMENT ON COLUMN payment.invoice_items.item_type IS
'Type of item being purchased (e.g., COURSE, WORKSHOP).';

COMMENT ON COLUMN payment.invoice_items.item_id IS
'UUID of the referenced item (e.g., course_id).';

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------
