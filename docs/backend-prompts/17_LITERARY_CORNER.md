# 17_LITERARY_CORNER.md

# HSSA Backend - Literary Corner Management Module

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
- 17_READING_CLUB.md

Implement ONLY the Literary Corner module.

Do NOT implement Workshops.

Do NOT implement Events.

Do NOT implement Notifications.

Stop after completing this module.

Wait for approval.

---

# Objective

Implement a complete enterprise-grade Literary Corner module.

The Literary Corner is a community platform where users can create, publish, share, review and discover literary works.

Support both academic and creative writing.

The module should encourage collaboration while maintaining moderation and quality.

---

# Dependencies

This module depends on

✔ Authentication

✔ User Management

✔ Media

✔ Publication

Reuse existing services.

Do not duplicate logic.

---

# Database

Read the existing database documentation.

Use ONLY the existing Literary Corner tables.

Do NOT create tables.

Do NOT rename tables.

Do NOT rename columns.

Do NOT modify relationships.

Generate backend code according to the existing schema.

---

# Package Structure

literary/

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

✔ Literary Work CRUD

✔ Draft Management

✔ Publishing Workflow

✔ Categories

✔ Genres

✔ Tags

✔ Authors

✔ Co-authors

✔ Reviews

✔ Ratings

✔ Comments

✔ Likes

✔ Favorites

✔ Bookmarks

✔ Search

✔ Filtering

✔ Pagination

✔ Featured Works

✔ Archive

✔ Soft Delete

✔ Restore

✔ Statistics

✔ Moderation Support

✔ Version Ready Design

---

# Supported Content

Support

Poems

Short Stories

Essays

Articles

Novels

Scripts

Drama

Book Reviews

Creative Writing

Academic Essays

Future-ready for additional literary content.

---

# APIs

Create REST APIs.

Examples

GET

/api/v1/literary

GET

/api/v1/literary/{id}

POST

/api/v1/literary

PUT

/api/v1/literary/{id}

PATCH

/api/v1/literary/{id}

DELETE

/api/v1/literary/{id}

PATCH

/api/v1/literary/{id}/publish

PATCH

/api/v1/literary/{id}/archive

GET

/api/v1/literary/search

GET

/api/v1/literary/featured

POST

/api/v1/literary/{id}/like

POST

/api/v1/literary/{id}/favorite

POST

/api/v1/literary/{id}/comment

POST

/api/v1/literary/{id}/review

GET

/api/v1/literary/statistics

---

# Business Rules

Only authenticated users may create literary works.

Authors own their works.

Drafts are private.

Published works are publicly visible according to visibility settings.

Moderators and administrators may remove inappropriate content.

Support multiple authors.

Soft delete only.

Maintain revision history.

Media files use Media Module.

---

# Integration

Integrate with

Authentication Module

User Module

Media Module

Publication Module

Notification Module

Do NOT implement Workshops.

---

# DTOs

Create

CreateLiteraryWorkRequest

UpdateLiteraryWorkRequest

PublishLiteraryWorkRequest

SearchLiteraryWorkRequest

ReviewRequest

CommentRequest

RatingRequest

LiteraryWorkResponse

LiteraryWorkSummaryResponse

LiteraryWorkDetailResponse

LiteraryStatisticsResponse

AuthorResponse

---

# Search

Support

Title

Author

Genre

Category

Tags

Status

Published Date

Rating

Keyword

Sorting

Pagination

Spring Specification

---

# Validation

Validate

Title

Genre

Category

Content

Authors

Media References

Status

Duplicate Authors

Duplicate Tags

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

LiteraryWorkCreatedEvent

LiteraryWorkUpdatedEvent

LiteraryWorkPublishedEvent

LiteraryWorkArchivedEvent

ReviewCreatedEvent

CommentCreatedEvent

FavoriteAddedEvent

LikeAddedEvent

---

# Security

Secure all APIs.

Use

@PreAuthorize

Examples

ROLE_ADMIN

ROLE_INSTRUCTOR

ROLE_USER

Only owners, moderators and administrators may edit or remove published works.

---

# Logging

Log

Literary Work Created

Literary Work Published

Review Added

Comment Added

Favorite Added

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

✔ Reviews

✔ Comments

✔ Favorites

✔ Ratings

✔ Security

✔ Media Integration

Nothing else.

---

# Completion Checklist

Verify

☑ CRUD complete

☑ Draft workflow complete

☑ Publishing complete

☑ Reviews complete

☑ Comments complete

☑ Ratings complete

☑ Favorites complete

☑ Search complete

☑ Pagination complete

☑ Security complete

☑ Existing database respected

☑ No Workshop implementation

Stop.

Wait for approval.

Next Module

18_WORKSHOP.md