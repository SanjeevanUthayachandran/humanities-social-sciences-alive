/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 02_extensions.sql
Version : 1.0
Author  : Sanjeevan Uthayachandran
==============================================================

Description:
Installs all PostgreSQL extensions required by the HSSA
Digital Education & Research Platform.

Execute after:
01_database.sql

==============================================================
*/

--------------------------------------------------------------
-- Connect Database
--------------------------------------------------------------
\connect hssa_db;


--------------------------------------------------------------
-- UUID Generation
--------------------------------------------------------------
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

COMMENT ON EXTENSION "uuid-ossp" IS
'UUID generation functions for primary keys.';


--------------------------------------------------------------
-- pgcrypto
--------------------------------------------------------------
CREATE EXTENSION IF NOT EXISTS pgcrypto;

COMMENT ON EXTENSION pgcrypto IS
'Cryptographic functions including gen_random_uuid(), hashing and encryption.';


--------------------------------------------------------------
-- Trigram Search
--------------------------------------------------------------
CREATE EXTENSION IF NOT EXISTS pg_trgm;

COMMENT ON EXTENSION pg_trgm IS
'Fast fuzzy text searching using trigram indexes.';


--------------------------------------------------------------
-- B-Tree GIN Index
--------------------------------------------------------------
CREATE EXTENSION IF NOT EXISTS btree_gin;

COMMENT ON EXTENSION btree_gin IS
'Additional indexing support for GIN indexes.';


--------------------------------------------------------------
-- B-Tree GiST Index
--------------------------------------------------------------
CREATE EXTENSION IF NOT EXISTS btree_gist;

COMMENT ON EXTENSION btree_gist IS
'Additional indexing support for GiST indexes.';


--------------------------------------------------------------
-- Unaccent Search
--------------------------------------------------------------
CREATE EXTENSION IF NOT EXISTS unaccent;

COMMENT ON EXTENSION unaccent IS
'Removes accents from text for improved searching.';


--------------------------------------------------------------
-- Case-Insensitive Text
--------------------------------------------------------------
CREATE EXTENSION IF NOT EXISTS citext;

COMMENT ON EXTENSION citext IS
'Provides case-insensitive text type (ideal for email addresses).';


--------------------------------------------------------------
-- XML Support
--------------------------------------------------------------
CREATE EXTENSION IF NOT EXISTS xml2;

COMMENT ON EXTENSION xml2 IS
'Additional XML processing functions.';


--------------------------------------------------------------
-- Statistics Collection
--------------------------------------------------------------
CREATE EXTENSION IF NOT EXISTS pg_stat_statements;

COMMENT ON EXTENSION pg_stat_statements IS
'Tracks execution statistics for SQL statements.';


--------------------------------------------------------------
-- Verify Installed Extensions
--------------------------------------------------------------
SELECT
    extname AS extension_name,
    extversion AS version
FROM pg_extension
ORDER BY extname;


--------------------------------------------------------------
-- End of File
--------------------------------------------------------------