# 15_PUBLICATION.md

# HSSA Backend - Publication Management Module

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

Implement ONLY the Publication Management module.

Do NOT implement Reading Club.

Do NOT implement Literary Corner.

Do NOT implement Workshops.

Stop after completing this module.

Wait for approval.

---

# Objective

Implement a complete enterprise-grade Publication Management module.

This module manages academic and literary publications, including journals, conference papers, books, book chapters, reports, magazines, newsletters and other scholarly works.

Research projects are managed in the Research module.

This module manages the publication lifecycle.

---

# Dependencies

This module depends on

✔ Authentication

✔ User Management

✔ Media

✔ Research

Reuse existing services.

Do not duplicate logic.

---

# Database

Read the existing database documentation.

Use ONLY the existing publication-related tables.

Do NOT create tables.

Do NOT rename tables.

Do NOT rename columns.

Do NOT modify relationships.

Generate backend code only according to the existing schema.

---

# Package Structure

publication/

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

✔ Publication CRUD

✔ Publication Categories

✔ Publication Types

✔ Publication Authors

✔ Co-authors

✔ Editors

✔ DOI Support

✔ ISBN Support

✔ ISSN Support

✔ Publisher Information

✔ Publication Status

✔ Publication Keywords

✔ Publication Tags

✔ Research Linking

✔ Publication Search

✔ Publication Filtering

✔ Publication Pagination

✔ Publication Statistics

✔ Publication Archive

✔ Soft Delete

✔ Restore

✔ Citation Ready Design

✔ Version Ready Design

---

# Supported Publication Types

Support

Journal Article

Conference Paper

Book

Book Chapter

Research Report

Magazine

Newsletter

White Paper

Working Paper

Thesis

Dissertation

Future-ready for additional publication types.

---

# APIs

Create REST APIs.

Examples

GET

/api/v1/publications

GET

/api/v1/publications/{id}

POST

/api/v1/publications

PUT

/api/v1/publications/{id}

PATCH

/api/v1/publications/{id}

DELETE

/api/v1/publications/{id}

PATCH

/api/v1/publications/{id}/archive

PATCH

/api/v1/publications/{id}/restore

GET

/api/v1/publications/search

GET

/api/v1/publications/statistics

GET

/api/v1/publications/{id}/authors

POST

/api/v1/publications/{id}/authors

DELETE

/api/v1/publications/{id}/authors/{authorId}

---

# Business Rules

Only authenticated users may create publications.

Publication owner controls publication.

ADMIN may manage all publications.

Publication title uniqueness follows project rules.

Soft delete only.

Publication files use Media Module.

Maintain publication history.

Support multiple authors.

Support multiple editors.

Link publications to research projects when applicable.

---

# Integration

Integrate with

Authentication Module

User Module

Research Module

Media Module

Notification Module

Do NOT implement Reading Club.

---

# DTOs

Create

CreatePublicationRequest

UpdatePublicationRequest

SearchPublicationRequest

AddAuthorRequest

RemoveAuthorRequest

PublicationResponse

PublicationSummaryResponse

PublicationDetailResponse

PublicationAuthorResponse

PublicationStatisticsResponse

---

# Search

Support

Title

Publication Type

Category

Author

Editor

Publisher

DOI

ISBN

ISSN

Keyword

Research Project

Publication Status

Publication Date

Sorting

Pagination

Spring Specification

---

# Validation

Validate

Publication Title

Publication Type

Authors

Publisher

DOI

ISBN

ISSN

Media References

Publication Date

Duplicate Authors

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

PublicationCreatedEvent

PublicationUpdatedEvent

PublicationArchivedEvent

PublicationDeletedEvent

PublicationPublishedEvent

AuthorAddedEvent

AuthorRemovedEvent

---

# Security

Secure all APIs.

Use

@PreAuthorize

Examples

ROLE_ADMIN

ROLE_INSTRUCTOR

ROLE_USER

Only publication owner, editors and administrators may modify publications.

Public users may view only published publications.

---

# Logging

Log

Publication Created

Publication Updated

Publication Published

Publication Archived

Publication Deleted

Author Added

Author Removed

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

✔ Research Integration

✔ Media Integration

Nothing else.

---

# Completion Checklist

Verify

☑ CRUD complete

☑ Author Management complete

☑ DOI/ISBN/ISSN support complete

☑ Search complete

☑ Filtering complete

☑ Pagination complete

☑ Statistics complete

☑ Security complete

☑ Existing database respected

☑ No Reading Club implementation

Stop.

Wait for approval.

Next Module

16_READING_CLUB.md