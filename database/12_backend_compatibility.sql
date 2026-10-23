/*
==============================================================
Backend compatibility migration
==============================================================

Aligns existing PostgreSQL installations with the JPA auditing
contract. Safe to run repeatedly.
*/

\connect hssa_db;

ALTER TABLE research.research_authors
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMPTZ,
    ADD COLUMN IF NOT EXISTS updated_by UUID;

ALTER TABLE publication.publication_authors
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMPTZ,
    ADD COLUMN IF NOT EXISTS updated_by UUID;

ALTER TABLE research.research_keywords
    ADD COLUMN IF NOT EXISTS version INTEGER NOT NULL DEFAULT 0;

ALTER TABLE publication.publication_keywords
    ADD COLUMN IF NOT EXISTS version INTEGER NOT NULL DEFAULT 0;

ALTER TABLE research.research_keyword_map
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMPTZ,
    ADD COLUMN IF NOT EXISTS updated_by UUID,
    ADD COLUMN IF NOT EXISTS version INTEGER NOT NULL DEFAULT 0;

ALTER TABLE publication.publication_keyword_map
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMPTZ,
    ADD COLUMN IF NOT EXISTS updated_by UUID,
    ADD COLUMN IF NOT EXISTS version INTEGER NOT NULL DEFAULT 0;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fk_research_authors_updated'
    ) THEN
        ALTER TABLE research.research_authors
            ADD CONSTRAINT fk_research_authors_updated
            FOREIGN KEY (updated_by) REFERENCES auth.users(id) ON DELETE SET NULL;
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fk_publication_authors_updated'
    ) THEN
        ALTER TABLE publication.publication_authors
            ADD CONSTRAINT fk_publication_authors_updated
            FOREIGN KEY (updated_by) REFERENCES auth.users(id) ON DELETE SET NULL;
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fk_rkm_updated'
    ) THEN
        ALTER TABLE research.research_keyword_map
            ADD CONSTRAINT fk_rkm_updated
            FOREIGN KEY (updated_by) REFERENCES auth.users(id) ON DELETE SET NULL;
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fk_pkm_updated'
    ) THEN
        ALTER TABLE publication.publication_keyword_map
            ADD CONSTRAINT fk_pkm_updated
            FOREIGN KEY (updated_by) REFERENCES auth.users(id) ON DELETE SET NULL;
    END IF;
END
$$;
