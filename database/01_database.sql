/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 01_database.sql
Version : 1.0
Author  : Sanjeevan Uthayachandran
==============================================================

Description:
Creates the HSSA production database and applies the initial
database configuration.

Execution:
psql -U postgres -f 01_database.sql
==============================================================
*/


---------------------------------------------------------------
-- Drop Database (Development Only)
-- Comment this section in Production
---------------------------------------------------------------
DROP DATABASE IF EXISTS hssa_db;


---------------------------------------------------------------
-- Create Database
---------------------------------------------------------------
CREATE DATABASE hssa_db
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'C'
    LC_CTYPE = 'C'
    TEMPLATE = template0
    CONNECTION LIMIT = -1;


---------------------------------------------------------------
-- Connect to Database
---------------------------------------------------------------
\connect hssa_db;


---------------------------------------------------------------
-- Database Information
---------------------------------------------------------------
COMMENT ON DATABASE hssa_db IS
'Humanities & Social Sciences Alive - Digital Education & Research Platform';


---------------------------------------------------------------
-- Set Default Schema
---------------------------------------------------------------
SET search_path TO public;


---------------------------------------------------------------
-- Set Timezone
---------------------------------------------------------------
SET TIME ZONE 'Asia/Colombo';


---------------------------------------------------------------
-- Client Encoding
---------------------------------------------------------------
SET client_encoding = 'UTF8';


---------------------------------------------------------------
-- Standard Conforming Strings
---------------------------------------------------------------
SET standard_conforming_strings = ON;


---------------------------------------------------------------
-- Date Style
---------------------------------------------------------------
SET datestyle = 'ISO, MDY';


---------------------------------------------------------------
-- Interval Style
---------------------------------------------------------------
SET intervalstyle = 'postgres';


---------------------------------------------------------------
-- Lock Timeout
---------------------------------------------------------------
SET lock_timeout = '30s';


---------------------------------------------------------------
-- Statement Timeout
---------------------------------------------------------------
SET statement_timeout = '5min';


---------------------------------------------------------------
-- Idle Transaction Timeout
---------------------------------------------------------------
SET idle_in_transaction_session_timeout = '10min';


---------------------------------------------------------------
-- Create Dedicated Schemas
---------------------------------------------------------------

CREATE SCHEMA IF NOT EXISTS auth;
CREATE SCHEMA IF NOT EXISTS lms;
CREATE SCHEMA IF NOT EXISTS research;
CREATE SCHEMA IF NOT EXISTS publication;
CREATE SCHEMA IF NOT EXISTS event;
CREATE SCHEMA IF NOT EXISTS payment;
CREATE SCHEMA IF NOT EXISTS notification;
CREATE SCHEMA IF NOT EXISTS media;
CREATE SCHEMA IF NOT EXISTS cms;
CREATE SCHEMA IF NOT EXISTS audit;


---------------------------------------------------------------
-- Schema Comments
---------------------------------------------------------------

COMMENT ON SCHEMA auth IS 'Authentication and Authorization';
COMMENT ON SCHEMA lms IS 'Learning Management System';
COMMENT ON SCHEMA research IS 'Research Management';
COMMENT ON SCHEMA publication IS 'Publication Repository';
COMMENT ON SCHEMA event IS 'Events and Workshops';
COMMENT ON SCHEMA payment IS 'Payment System';
COMMENT ON SCHEMA notification IS 'Notification Module';
COMMENT ON SCHEMA media IS 'Media Storage';
COMMENT ON SCHEMA cms IS 'Content Management';
COMMENT ON SCHEMA audit IS 'Audit Logs';


---------------------------------------------------------------
-- Database Settings
---------------------------------------------------------------

ALTER DATABASE hssa_db SET timezone TO 'Asia/Colombo';
ALTER DATABASE hssa_db SET search_path TO public;
ALTER DATABASE hssa_db SET default_transaction_isolation TO 'read committed';


---------------------------------------------------------------
-- End of File
---------------------------------------------------------------