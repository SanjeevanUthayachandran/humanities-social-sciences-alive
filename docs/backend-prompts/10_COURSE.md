# 10_COURSE.md

# HSSA Backend - Course Management Module

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

Implement ONLY the Course Management module.

Do NOT implement Lesson Management.

Do NOT implement Enrollment.

Do NOT implement Certificates.

Stop after completing this module.

Wait for approval.

---

# Objective

Implement a complete enterprise-grade Course Management module.

This module is responsible for creating, organizing, publishing and managing courses.

All course-related business logic belongs here.

Lesson implementation belongs to the next module.

---

# Database

Read the existing database documentation before generating code.

Use ONLY the existing database schema.

Do NOT create tables.

Do NOT rename tables.

Do NOT rename columns.

Do NOT modify relationships.

Generate only code that maps to the existing database.

---

# Package Structure

course/

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

✔ Course CRUD

✔ Course Categories

✔ Course Tags

✔ Course Levels

✔ Course Languages

✔ Course Status

✔ Course Visibility

✔ Course Thumbnail

✔ Course Banner

✔ Instructor Assignment

✔ Course Publishing

✔ Course Unpublishing

✔ Archive Course

✔ Restore Course

✔ Soft Delete

✔ Course Search

✔ Course Filtering

✔ Course Sorting

✔ Course Pagination

✔ Featured Courses

✔ Course Statistics

✔ Course Metadata

✔ Course SEO Information

✔ Course Slug Generation

✔ Course Versioning (future ready)

---

# APIs

Create REST APIs.

Examples

GET

/api/v1/courses

GET

/api/v1/courses/{id}

GET

/api/v1/courses/slug/{slug}

POST

/api/v1/courses

PUT

/api/v1/courses/{id}

PATCH

/api/v1/courses/{id}

PATCH

/api/v1/courses/{id}/publish

PATCH

/api/v1/courses/{id}/unpublish

PATCH

/api/v1/courses/{id}/archive

PATCH

/api/v1/courses/{id}/restore

DELETE

/api/v1/courses/{id}

GET

/api/v1/courses/search

GET

/api/v1/courses/featured

GET

/api/v1/courses/statistics

---

# Business Rules

Only ADMIN and INSTRUCTOR can create courses.

Only owner or ADMIN can update.

Published courses cannot be permanently deleted.

Soft delete only.

Course title must be unique according to project rules.

Slug must be unique.

Thumbnail uses Media Module.

Banner uses Media Module.

Status changes must be validated.

---

# Integration

Integrate with

Media Module

for

- Thumbnail
- Banner
- Promotional Images

Integrate with

User Module

for

- Instructor

Do not implement Lessons.

---

# DTOs

Create

CreateCourseRequest

UpdateCourseRequest

PublishCourseRequest

SearchCourseRequest

CourseResponse

CourseSummaryResponse

CourseDetailResponse

CourseStatisticsResponse

FeaturedCourseResponse

---

# Search

Support

Keyword

Category

Instructor

Level

Language

Status

Visibility

Featured

Published

Date Range

Sorting

Pagination

Spring Specification

---

# Validation

Validate

Course Title

Slug

Category

Instructor

Thumbnail

Language

Level

Visibility

Status

SEO fields

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

CourseCreatedEvent

CourseUpdatedEvent

CoursePublishedEvent

CourseArchivedEvent

CourseDeletedEvent

---

# Security

Secure all APIs.

Use

@PreAuthorize

Examples

ROLE_ADMIN

ROLE_INSTRUCTOR

Public users may only view published courses.

---

# Logging

Log

Course Created

Course Updated

Course Published

Course Archived

Course Deleted

Course Restored

Never log sensitive data.

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

✔ Integration with Media

✔ Integration with User

Nothing else.

---

# Completion Checklist

Verify

☑ CRUD complete

☑ Search complete

☑ Filtering complete

☑ Pagination complete

☑ Publish workflow complete

☑ Archive workflow complete

☑ Soft Delete complete

☑ Media integration complete

☑ Security complete

☑ Existing database respected

☑ No Lesson implementation

Stop.

Wait for approval.

Next Module

11_LESSON.md