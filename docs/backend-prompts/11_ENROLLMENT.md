# 11_ENROLLMENT.md

# HSSA Backend - Enrollment Management Module

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

Implement ONLY the Enrollment Management module.

Do NOT implement Payments.

Do NOT implement Certificates.

Do NOT implement Progress Tracking.

Stop after completing this module.

Wait for approval.

---

# Objective

Implement a complete enterprise-grade Enrollment Management module.

This module manages the relationship between users and courses.

A user can enroll in multiple courses.

A course can have multiple enrolled users.

Enrollment is the foundation for learning progress, certificates, and course access.

Do NOT implement lesson completion or certificate generation.

---

# Database

Read the existing database documentation before generating code.

Use ONLY the existing enrollment-related tables.

Do NOT create tables.

Do NOT rename tables.

Do NOT rename columns.

Do NOT modify relationships.

Generate only backend code that maps to the existing database.

---

# Package Structure

enrollment/

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

---

# Responsibilities

Implement

✔ Course Enrollment

✔ Cancel Enrollment

✔ Withdraw Enrollment

✔ Re-enrollment

✔ Enrollment Approval

✔ Enrollment Rejection

✔ Enrollment Status

✔ Enrollment History

✔ Enrollment Search

✔ Enrollment Filtering

✔ Enrollment Sorting

✔ Enrollment Pagination

✔ Student Course List

✔ Course Student List

✔ Enrollment Statistics

✔ Enrollment Validation

✔ Course Access Verification

✔ Enrollment Expiration (future ready)

✔ Waitlist Support (future ready)

---

# APIs

Create REST APIs.

Examples

GET

/api/v1/enrollments

GET

/api/v1/enrollments/{id}

POST

/api/v1/enrollments

DELETE

/api/v1/enrollments/{id}

PATCH

/api/v1/enrollments/{id}/approve

PATCH

/api/v1/enrollments/{id}/reject

PATCH

/api/v1/enrollments/{id}/withdraw

GET

/api/v1/users/{userId}/courses

GET

/api/v1/courses/{courseId}/students

GET

/api/v1/enrollments/search

GET

/api/v1/enrollments/statistics

---

# Business Rules

Only authenticated users can enroll.

A user cannot enroll twice in the same course.

Enrollment requires an active account.

Only published courses can accept enrollments.

Enrollment must reference an existing course.

Enrollment must reference an existing user.

Only ADMIN or INSTRUCTOR may approve enrollments if approval is required.

Soft delete only.

Maintain enrollment history.

---

# Integration

Integrate with

Authentication Module

User Module

Course Module

Lesson Module

Do NOT implement

Payments

Certificates

Learning Progress

Those belong to later modules.

---

# DTOs

Create

CreateEnrollmentRequest

UpdateEnrollmentRequest

ApproveEnrollmentRequest

RejectEnrollmentRequest

WithdrawEnrollmentRequest

SearchEnrollmentRequest

EnrollmentResponse

EnrollmentSummaryResponse

EnrollmentDetailResponse

EnrollmentStatisticsResponse

StudentCourseResponse

CourseStudentResponse

---

# Search

Support

Student

Course

Instructor

Status

Enrollment Date

Approval Status

Keyword

Sorting

Pagination

Spring Specification

---

# Validation

Validate

User ID

Course ID

Enrollment Status

Approval Status

Date Range

Pagination

Search Parameters

Duplicate Enrollment

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

EnrollmentCreatedEvent

EnrollmentApprovedEvent

EnrollmentRejectedEvent

EnrollmentWithdrawnEvent

EnrollmentCancelledEvent

---

# Security

Secure all APIs.

Use

@PreAuthorize

Examples

ROLE_ADMIN

ROLE_INSTRUCTOR

ROLE_USER

Only the enrolled student or authorized staff may access enrollment details.

---

# Logging

Log

Enrollment Created

Enrollment Approved

Enrollment Rejected

Enrollment Withdrawn

Enrollment Cancelled

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

✔ Integration with User

✔ Integration with Course

✔ Integration with Lesson

Nothing else.

---

# Completion Checklist

Verify

☑ Course enrollment complete

☑ Withdrawal complete

☑ Approval workflow complete

☑ Search complete

☑ Filtering complete

☑ Pagination complete

☑ Enrollment validation complete

☑ Security complete

☑ Existing database respected

☑ No Payment implementation

☑ No Certificate implementation

☑ No Learning Progress implementation

Stop.

Wait for approval.

Next Module

12_PAYMENT.md