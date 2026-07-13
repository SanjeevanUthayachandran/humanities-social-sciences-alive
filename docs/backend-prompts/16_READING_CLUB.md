# 16_READING_CLUB.md

# HSSA Backend - Reading Club Management Module

Follow ALL previous prompts.

- 01_GLOBAL_RULES.md
- 02_PROJECT_SETUP.md
- 03_COMMON_INFRASTRUCTURE.md
- 04_CONFIGURATION.md
- 05_EXCEPTION_HANDLING.md
- 06_SECURITY.md
- 07_AUTHENTICATION.md
- 08_USER_MANAGEMENT.md
- 09_MEDIA.md
- 10_COURSE.md
- 11_LESSON.md
- 12_ENROLLMENT.md
- 13_PAYMENT.md
- 14_CERTIFICATE.md
- 15_RESEARCH.md
- 16_PUBLICATION.md

Implement ONLY the Reading Club Management module.

Do NOT implement Literary Corner.

Do NOT implement Workshops.

Do NOT implement Events.

Stop after completing this module.

Wait for approval.

---

# Objective

Implement a complete enterprise-grade Reading Club Management module.

The Reading Club module enables users to discover books, organize reading clubs, join reading groups, schedule reading sessions, participate in discussions, share reviews, and track reading activities.

The module should support both physical and virtual reading clubs.

---

# Dependencies

This module depends on

✔ Authentication

✔ User Management

✔ Media

✔ Notification

✔ Publication

Reuse existing services.

Do not duplicate logic.

---

# Database

Read the existing database documentation.

Use ONLY the existing Reading Club tables.

Do NOT create tables.

Do NOT rename tables.

Do NOT rename columns.

Do NOT modify relationships.

Generate backend code according to the existing schema.

---

# Package Structure

readingclub/

controller/

service/
    impl/

repository/

entity/

dto/
    request/
    response/

mapper/

validator/

exception/

event/

specification/

---

# Responsibilities

Implement

✔ Reading Club CRUD

✔ Reading Club Categories

✔ Reading Club Membership

✔ Membership Requests

✔ Membership Approval

✔ Reading Sessions

✔ Session Scheduling

✔ Session Attendance

✔ Discussion Topics

✔ Book Discussions

✔ Reading Progress

✔ Reading Lists

✔ Book Recommendations

✔ Reviews

✔ Ratings

✔ Reading Challenges

✔ Reading Club Search

✔ Filtering

✔ Pagination

✔ Statistics

✔ Archive

✔ Soft Delete

✔ Restore

---

# APIs

Create REST APIs.

Examples

GET

/api/v1/reading-clubs

GET

/api/v1/reading-clubs/{id}

POST

/api/v1/reading-clubs

PUT

/api/v1/reading-clubs/{id}

PATCH

/api/v1/reading-clubs/{id}

DELETE

/api/v1/reading-clubs/{id}

POST

/api/v1/reading-clubs/{id}/join

POST

/api/v1/reading-clubs/{id}/leave

POST

/api/v1/reading-clubs/{id}/approve-member

POST

/api/v1/reading-clubs/{id}/sessions

GET

/api/v1/reading-clubs/{id}/members

GET

/api/v1/reading-clubs/search

GET

/api/v1/reading-clubs/statistics

---

# Business Rules

Only authenticated users can create or join reading clubs.

Club owners and administrators manage memberships.

A user cannot join the same reading club twice.

Reading clubs may be Public or Private.

Private clubs require approval.

Books may be linked to Publications.

Maintain reading history.

Support recurring reading sessions.

Soft delete only.

---

# Integration

Integrate with

Authentication Module

User Module

Publication Module

Media Module

Notification Module

Do NOT implement Literary Corner.

---

# DTOs

Create

CreateReadingClubRequest

UpdateReadingClubRequest

JoinReadingClubRequest

MembershipApprovalRequest

CreateReadingSessionRequest

SearchReadingClubRequest

ReadingClubResponse

ReadingClubSummaryResponse

ReadingClubDetailResponse

ReadingSessionResponse

ReadingMemberResponse

ReadingClubStatisticsResponse

---

# Search

Support

Club Name

Book

Category

Organizer

Member

Status

Visibility

Tags

Date Range

Sorting

Pagination

Spring Specification

---

# Validation

Validate

Club Name

Description

Meeting Date

Capacity

Book References

Membership

Session Dates

Visibility

Duplicate Membership

---

# Repository

Use Spring Data JPA.

Support

Specifications

Pagination

Sorting

Filtering

Avoid unnecessary native SQL.

---

# Service Layer

Business logic belongs only in services.

Controllers remain thin.

Use transactions appropriately.

Return DTOs only.

Never expose entities.

---

# Events

Create

ReadingClubCreatedEvent

ReadingClubUpdatedEvent

ReadingClubDeletedEvent

MemberJoinedEvent

MemberLeftEvent

MembershipApprovedEvent

ReadingSessionCreatedEvent

DiscussionCreatedEvent

---

# Security

Secure all APIs.

Use

@PreAuthorize

Examples

ROLE_ADMIN

ROLE_INSTRUCTOR

ROLE_USER

Only club owners, moderators, and administrators may manage club settings.

---

# Logging

Log

Reading Club Created

Reading Club Updated

Member Joined

Member Left

Session Created

Discussion Created

Never log sensitive information.

---

# Documentation

Every class

Every method

Every public API

must contain JavaDoc.

---

# Unit Testing

Generate test skeletons.

Do not implement test logic.

---

# Deliverables

Generate

✔ Controllers

✔ Services

✔ Repositories

✔ DTOs

✔ Validators

✔ Specifications

✔ Events

✔ Search

✔ Filtering

✔ Pagination

✔ Security

✔ Publication Integration

✔ Notification Integration

Nothing else.

---

# Completion Checklist

Verify

☑ CRUD complete

☑ Membership complete

☑ Reading Sessions complete

☑ Discussions complete

☑ Search complete

☑ Filtering complete

☑ Pagination complete

☑ Statistics complete

☑ Security complete

☑ Existing database respected

☑ No Literary Corner implementation

Stop.

Wait for approval.

Next Module

17_LITERARY_CORNER.md