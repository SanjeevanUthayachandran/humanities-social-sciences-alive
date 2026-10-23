# Humanities & Social Sciences Alive (HSSA)

## Enterprise PostgreSQL Database

---

## Project Overview

The **Humanities & Social Sciences Alive (HSSA)** database is an enterprise-level PostgreSQL database designed for an online educational, research, and publication platform.

The system supports:

- Learning Management System (LMS)
- Course Management
- Lesson Management
- Enrollment
- Online Payments
- Certificates
- Research Repository
- Publication Repository
- Workshops
- Events
- Reading Club
- Literary Corner
- News & Announcements
- Notification System
- Media Library
- Contact Management
- Authentication & Authorization (RBAC)
- Audit Logging

---

# Technology

| Component | Technology |
|------------|------------|
| Database | PostgreSQL 16+ |
| Backend | Spring Boot 3 |
| ORM | Spring Data JPA / Hibernate |
| Authentication | JWT |
| Authorization | RBAC |
| UUID | gen_random_uuid() |
| Password | BCrypt |
| File Storage | AWS S3 / Local |

---

# PostgreSQL Extensions

- pgcrypto
- citext
- uuid-ossp (optional)

---

# Database Schemas

```
auth
lms
payment
research
publication
event
cms
media
notification
audit
common
public
```

---

# Folder Structure

```
database/

01_database.sql

02_extensions.sql

03_types.sql

04_tables/

    04_01_auth/
        04_01_auth_users.sql
        04_02_auth_roles.sql
        04_03_auth_permissions.sql
        04_04_auth_tokens.sql
        04_05_auth_sessions.sql
        04_06_auth_role_permissions.sql
        04_07_auth_user_roles.sql

    04_02_users.sql

    04_03_courses/

    04_04_lessons/

    04_05_enrollment/

    04_06_payment/

    04_07_certificates/

    04_08_research/

    04_09_publications/

    04_10_workshops/

    04_11_events/

    04_12_reading_club.sql

    04_13_literary.sql

    04_14_news.sql

    04_15_notifications.sql

    04_16_contact.sql

    04_17_media.sql

    04_18_audit.sql

05_constraints.sql

06_indexes.sql

07_triggers.sql

08_seed_roles.sql

09_seed_admin.sql

10_views.sql

11_functions.sql

README.md
```

---

# Authentication Module

The authentication module provides:

- User Registration
- Login
- Logout
- Email Verification
- Password Reset
- JWT Refresh Tokens
- Login History
- Session Management
- Role-Based Access Control (RBAC)

Roles

- ADMIN
- INSTRUCTOR
- USER

---

# Core Modules

- Authentication
- User Management
- Roles & Permissions
- Course Management
- Lessons
- Enrollment
- Payments
- Certificates
- Research
- Publications
- Workshops
- Events
- Reading Club
- Literary Corner
- News
- Notifications
- Contact
- Media
- Audit

---

# Database Design

Features

- UUID Primary Keys
- Foreign Keys
- Constraints
- Indexes
- Views
- Triggers
- Functions
- Seed Data
- Audit Logs

---

# Execution Order

Execute SQL files in the following order.

```
01_database.sql

02_extensions.sql

03_types.sql

04_tables/

05_constraints.sql

06_indexes.sql

07_functions.sql

08_triggers.sql

09_seed_roles.sql

10_seed_admin.sql

11_views.sql

12_backend_compatibility.sql
```

---

# Security

The database implements:

- BCrypt Password Hashing
- JWT Authentication
- Refresh Tokens
- Email Verification
- Password Reset
- RBAC Authorization
- Audit Logging

---

# Naming Conventions

## Tables

```
snake_case
```

Example

```
course_categories
```

---

## Columns

```
snake_case
```

Example

```
created_at
```

---

## Primary Keys

```
id UUID
```

---

## Foreign Keys

```
user_id

course_id

category_id
```

---

## Indexes

```
idx_table_column
```

Example

```
idx_users_email
```

---

## Constraints

```
pk_

fk_

chk_

uq_
```

---

# Development Guidelines

- Always use UUID as the primary key.
- Never store plain-text passwords.
- Always use foreign keys.
- Create indexes for searchable columns.
- Use ENUM types whenever possible.
- Use audit logging for important operations.
- Keep business logic inside Spring Boot.
- Keep PostgreSQL functions generic and reusable.

---

# Future Enhancements

- Full Text Search
- Elasticsearch Integration
- Redis Cache
- Database Partitioning
- Read Replicas
- Materialized Views
- Automated Backup
- Multi-language Support
- AI Recommendation Engine

---

# Author

**Sanjeevan Uthayachandran**

Department of Computer Engineering

University of Peradeniya

Sri Lanka

---

# License

This project is developed for academic and educational purposes.

Copyright © 2026
Humanities & Social Sciences Alive (HSSA)

All Rights Reserved.
