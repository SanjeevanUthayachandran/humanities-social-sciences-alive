# 14_RESEARCH.md

# HSSA Backend - Research Management Module

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

Implement ONLY the Research Management module.

Do NOT implement Publications.

Do NOT implement Reading Club.

Do NOT implement Literary Corner.

Stop after completing this module.

Wait for approval.

---

# Objective

Implement a complete enterprise-grade Research Management module.

This module manages research projects, research groups, research documents, supervisors, researchers, collaborators, milestones and research lifecycle.

The module must support individual and collaborative research.

Research publications are NOT handled here.

They belong to the Publication module.

---

# Dependencies

This module depends on

✔ Authentication

✔ User Management

✔ Media

Reuse existing services.

Do not duplicate logic.

---

# Database

Read the existing database documentation.

Use ONLY existing research-related tables.

Do NOT create tables.

Do NOT rename tables.

Do NOT rename columns.

Do NOT modify relationships.

Generate only backend code according to the existing schema.

---

# Package Structure

research/

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

✔ Research Project CRUD

✔ Research Group Management

✔ Research Members

✔ Research Supervisors

✔ Research Collaborators

✔ Research Documents

✔ Research Proposal

✔ Research Milestones

✔ Research Status

✔ Research Categories

✔ Research Keywords

✔ Research Search

✔ Research Filtering

✔ Research Pagination

✔ Research Statistics

✔ Research Timeline

✔ Research Archive

✔ Soft Delete

✔ Restore

✔ Version Ready Design

---

# APIs

Create REST APIs.

Examples

GET

/api/v1/research

GET

/api/v1/research/{id}

POST

/api/v1/research

PUT

/api/v1/research/{id}

PATCH

/api/v1/research/{id}

DELETE

/api/v1/research/{id}

PATCH

/api/v1/research/{id}/archive

PATCH

/api/v1/research/{id}/restore

GET

/api/v1/research/search

GET

/api/v1/research/statistics

GET

/api/v1/research/{id}/members

POST

/api/v1/research/{id}/members

DELETE

/api/v1/research/{id}/members/{memberId}

---

# Business Rules

Only authenticated users may create research.

Research owner controls project.

ADMIN may manage all projects.

Research title uniqueness follows project rules.

Soft delete only.

Research documents use Media Module.

Maintain complete research history.

Support multiple supervisors.

Support multiple collaborators.

---

# Integration

Integrate with

Authentication Module

User Module

Media Module

Notification Module

Do NOT implement Publications.

---

# DTOs

Create

CreateResearchRequest

UpdateResearchRequest

SearchResearchRequest

AddMemberRequest

RemoveMemberRequest

ResearchResponse

ResearchSummaryResponse

ResearchDetailResponse

ResearchMemberResponse

ResearchStatisticsResponse

---

# Search

Support

Title

Research Area

Category

Supervisor

Researcher

Status

Keyword

Date Range

Sorting

Pagination

Spring Specification

---

# Validation

Validate

Research Title

Category

Status

Supervisor

Members

Media References

Dates

Duplicate Members

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

ResearchCreatedEvent

ResearchUpdatedEvent

ResearchArchivedEvent

ResearchDeletedEvent

ResearchMemberAddedEvent

ResearchMemberRemovedEvent

ResearchStatusChangedEvent

---

# Security

Secure all APIs.

Use

@PreAuthorize

Examples

ROLE_ADMIN

ROLE_INSTRUCTOR

ROLE_USER

Only project owner, supervisors and administrators may modify research.

Public users may view only publicly visible research.

---

# Logging

Log

Research Created

Research Updated

Research Archived

Research Deleted

Member Added

Member Removed

Status Changed

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

✔ Media Integration

✔ User Integration

Nothing else.

---

# Completion Checklist

Verify

☑ CRUD complete

☑ Member Management complete

☑ Supervisor Management complete

☑ Search complete

☑ Filtering complete

☑ Pagination complete

☑ Statistics complete

☑ Security complete

☑ Existing database respected

☑ No Publication implementation

Stop.

Wait for approval.

Next Module

15_PUBLICATION.md