# 18_WORKSHOP.md

# HSSA Backend - Workshop Management Module

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
- 18_LITERARY_CORNER.md

Implement ONLY the Workshop Management module.

Do NOT implement Events.

Do NOT implement Notifications.

Do NOT implement Dashboard.

Stop after completing this module.

Wait for approval.

---

# Objective

Implement a complete enterprise-grade Workshop Management module.

This module manages academic, professional, cultural and community workshops.

Support both online and physical workshops.

Support free and paid workshops.

The module must handle the complete workshop lifecycle from creation to completion.

---

# Dependencies

This module depends on

✔ Authentication

✔ User Management

✔ Media

✔ Payment

✔ Notification

Reuse existing services.

Do not duplicate logic.

---

# Database

Read the existing database documentation.

Use ONLY the existing workshop-related tables.

Do NOT create tables.

Do NOT rename tables.

Do NOT rename columns.

Do NOT modify relationships.

Generate backend code according to the existing schema.

---

# Package Structure

workshop/

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

✔ Workshop CRUD

✔ Workshop Categories

✔ Workshop Types

✔ Organizers

✔ Speakers

✔ Resource Persons

✔ Registration

✔ Registration Approval

✔ Attendance

✔ Capacity Management

✔ Waitlist Support

✔ Workshop Schedule

✔ Sessions

✔ Agenda

✔ Venue Management

✔ Online Meeting Information

✔ Search

✔ Filtering

✔ Pagination

✔ Statistics

✔ Archive

✔ Soft Delete

✔ Restore

✔ Featured Workshops

✔ Feedback

✔ Ratings

---

# Workshop Types

Support

Academic Workshop

Research Workshop

Writing Workshop

Training Program

Technical Workshop

Online Webinar

Seminar

Panel Discussion

Guest Lecture

Hybrid Workshop

Future-ready for additional types.

---

# APIs

Create REST APIs.

Examples

GET

/api/v1/workshops

GET

/api/v1/workshops/{id}

POST

/api/v1/workshops

PUT

/api/v1/workshops/{id}

PATCH

/api/v1/workshops/{id}

DELETE

/api/v1/workshops/{id}

POST

/api/v1/workshops/{id}/register

DELETE

/api/v1/workshops/{id}/register

PATCH

/api/v1/workshops/{id}/approve

PATCH

/api/v1/workshops/{id}/attendance

GET

/api/v1/workshops/search

GET

/api/v1/workshops/featured

GET

/api/v1/workshops/statistics

---

# Business Rules

Only authenticated users may register.

Only ADMIN and ORGANIZER may create workshops.

Paid workshops require successful payment.

Capacity must not be exceeded.

Support waiting list.

Attendance recorded once.

Soft delete only.

Workshop media uses Media Module.

Registration history maintained.

---

# Integration

Integrate with

Authentication Module

User Module

Media Module

Payment Module

Notification Module

Do NOT implement Event Management.

---

# DTOs

Create

CreateWorkshopRequest

UpdateWorkshopRequest

WorkshopRegistrationRequest

AttendanceRequest

WorkshopFeedbackRequest

SearchWorkshopRequest

WorkshopResponse

WorkshopSummaryResponse

WorkshopDetailResponse

WorkshopRegistrationResponse

WorkshopStatisticsResponse

WorkshopAttendanceResponse

---

# Search

Support

Title

Category

Organizer

Speaker

Location

Workshop Type

Status

Date

Tags

Keyword

Sorting

Pagination

Spring Specification

---

# Validation

Validate

Workshop Title

Category

Workshop Type

Capacity

Registration Dates

Schedule

Venue

Online Link

Media References

Duplicate Registration

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

WorkshopCreatedEvent

WorkshopUpdatedEvent

WorkshopPublishedEvent

WorkshopRegistrationEvent

WorkshopAttendanceEvent

WorkshopCancelledEvent

WorkshopCompletedEvent

WorkshopFeedbackEvent

---

# Security

Secure all APIs.

Use

@PreAuthorize

Examples

ROLE_ADMIN

ROLE_INSTRUCTOR

ROLE_USER

ROLE_ORGANIZER

Only organizers and administrators may manage workshops.

---

# Logging

Log

Workshop Created

Workshop Updated

Registration

Attendance

Workshop Cancelled

Workshop Completed

Feedback Submitted

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

✔ Registration

✔ Attendance

✔ Feedback

✔ Search

✔ Filtering

✔ Pagination

✔ Security

✔ Media Integration

✔ Payment Integration

✔ Notification Integration

Nothing else.

---

# Completion Checklist

Verify

☑ CRUD complete

☑ Registration complete

☑ Attendance complete

☑ Capacity Management complete

☑ Feedback complete

☑ Search complete

☑ Pagination complete

☑ Security complete

☑ Existing database respected

☑ No Event implementation

Stop.

Wait for approval.

Next Module

19_EVENT.md