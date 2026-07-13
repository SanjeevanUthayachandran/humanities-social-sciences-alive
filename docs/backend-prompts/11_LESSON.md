# 11_LESSON.md

# HSSA Backend - Lesson Management Module

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

Implement ONLY the Lesson Management module.

Do NOT implement Enrollment.

Do NOT implement Certificates.

Do NOT implement Payments.

Stop after completing this module.

Wait for approval.

---

# Objective

Implement the complete Lesson Management module.

This module manages all learning content inside a course.

A lesson belongs to a course.

A course contains multiple lessons.

The lesson module must support multiple learning resource types while remaining independent from enrollment and progress tracking.

---

# Database

Read the existing database documentation.

Use ONLY existing lesson-related tables.

Do NOT create tables.

Do NOT rename tables.

Do NOT rename columns.

Do NOT modify relationships.

Generate only backend code that maps to the existing database.

---

# Package Structure

lesson/

```
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

specification/

event/
```

---

# Responsibilities

Implement

✔ Lesson CRUD

✔ Lesson Ordering

✔ Lesson Sections

✔ Lesson Resources

✔ Lesson Videos

✔ Lesson Documents

✔ Lesson Attachments

✔ Lesson Visibility

✔ Lesson Status

✔ Draft Lessons

✔ Published Lessons

✔ Lesson Search

✔ Lesson Filtering

✔ Lesson Sorting

✔ Lesson Pagination

✔ Preview Lessons

✔ Lesson Duration

✔ Lesson Metadata

✔ Lesson Prerequisites

✔ Lesson Notes

✔ Lesson Versioning (future ready)

---

# Lesson Types

Support

Video Lesson

Reading Lesson

PDF Lesson

Presentation Lesson

External Link

Downloadable Resource

Mixed Content

Future-ready for additional lesson types.

---

# APIs

Create REST APIs.

Examples

GET

/api/v1/lessons

GET

/api/v1/lessons/{id}

POST

/api/v1/lessons

PUT

/api/v1/lessons/{id}

PATCH

/api/v1/lessons/{id}

DELETE

/api/v1/lessons/{id}

PATCH

/api/v1/lessons/{id}/publish

PATCH

/api/v1/lessons/{id}/unpublish

PATCH

/api/v1/lessons/{id}/reorder

GET

/api/v1/lessons/search

GET

/api/v1/courses/{courseId}/lessons

---

# Business Rules

Only ADMIN and INSTRUCTOR may create lessons.

A lesson must belong to an existing course.

Lesson order must be unique within a course.

Published lessons cannot belong to unpublished courses.

Soft delete only.

Resources use the Media Module.

---

# Integration

Integrate with

Course Module

Media Module

User Module

Do NOT implement enrollment.

Do NOT implement lesson completion.

That belongs to Enrollment module.

---

# DTOs

Create

CreateLessonRequest

UpdateLessonRequest

ReorderLessonRequest

SearchLessonRequest

LessonResponse

LessonSummaryResponse

LessonDetailResponse

LessonResourceResponse

LessonStatisticsResponse

---

# Search

Support

Course

Lesson Type

Status

Visibility

Instructor

Keyword

Duration

Date

Sorting

Pagination

Spring Specification

---

# Validation

Validate

Course ID

Lesson Title

Lesson Order

Lesson Type

Media References

Visibility

Status

Duration

Metadata

---

# Repository

Use Spring Data JPA.

Support

Specifications

Pagination

Sorting

Filtering

No unnecessary native SQL.

---

# Service Layer

Business logic belongs only in services.

Controllers must remain thin.

Use transactions appropriately.

Return DTOs only.

Never expose entities.

---

# Events

Create

LessonCreatedEvent

LessonUpdatedEvent

LessonPublishedEvent

LessonDeletedEvent

LessonReorderedEvent

---

# Security

Secure all APIs.

Use

@PreAuthorize

ROLE_ADMIN

ROLE_INSTRUCTOR

Public users may only access published lessons belonging to published courses.

---

# Logging

Log

Lesson Created

Lesson Updated

Lesson Deleted

Lesson Published

Lesson Reordered

Never log sensitive information.

---

# Documentation

Every class

Every method

Every public API

must include JavaDoc.

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

✔ Specifications

✔ Validators

✔ Mappers

✔ Events

✔ Search

✔ Filtering

✔ Pagination

✔ Security

✔ Integration with Course

✔ Integration with Media

Nothing else.

---

# Completion Checklist

Verify

☑ CRUD complete

☑ Search complete

☑ Filtering complete

☑ Pagination complete

☑ Lesson ordering complete

☑ Resource management complete

☑ Media integration complete

☑ Security complete

☑ Existing database respected

☑ No Enrollment implementation

Stop.

Wait for approval.

Next Module

11_ENROLLMENT.md