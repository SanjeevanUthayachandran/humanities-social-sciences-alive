# 19_EVENT.md

# HSSA Backend - Event Management Module

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
- 19_WORKSHOP.md

Implement ONLY the Event Management module.

Do NOT implement Notifications.

Do NOT implement Contact.

Do NOT implement Dashboard.

Stop after completing this module.

Wait for approval.

---

# Objective

Implement a complete enterprise-grade Event Management module.

This module manages academic, institutional, cultural, social and community events.

Support physical, online and hybrid events.

Support free and paid events.

The module manages the complete event lifecycle from planning to completion.

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

Use ONLY the existing event-related tables.

Do NOT create tables.

Do NOT rename tables.

Do NOT rename columns.

Do NOT modify relationships.

Generate backend code according to the existing schema.

---

# Package Structure

event/

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

✔ Event CRUD

✔ Event Categories

✔ Event Types

✔ Organizers

✔ Speakers

✔ Guests

✔ Registration

✔ Registration Approval

✔ Attendance

✔ Capacity Management

✔ Waitlist

✔ Event Schedule

✔ Sessions

✔ Agenda

✔ Venue Management

✔ Online Meeting Support

✔ Search

✔ Filtering

✔ Pagination

✔ Statistics

✔ Featured Events

✔ Archive

✔ Restore

✔ Soft Delete

✔ Feedback

✔ Ratings

✔ Event Gallery (Media)

---

# Supported Event Types

Support

Conference

Seminar

Symposium

Guest Lecture

Webinar

Competition

Exhibition

Festival

Award Ceremony

Community Event

Networking Event

Orientation Program

Career Fair

Future-ready for additional event types.

---

# APIs

Create REST APIs.

Examples

GET

/api/v1/events

GET

/api/v1/events/{id}

POST

/api/v1/events

PUT

/api/v1/events/{id}

PATCH

/api/v1/events/{id}

DELETE

/api/v1/events/{id}

POST

/api/v1/events/{id}/register

DELETE

/api/v1/events/{id}/register

PATCH

/api/v1/events/{id}/attendance

PATCH

/api/v1/events/{id}/publish

PATCH

/api/v1/events/{id}/cancel

GET

/api/v1/events/search

GET

/api/v1/events/upcoming

GET

/api/v1/events/featured

GET

/api/v1/events/statistics

---

# Business Rules

Only authenticated users may register.

Only ADMIN and EVENT ORGANIZER may create events.

Paid events require successful payment.

Capacity must not be exceeded.

Support waiting list.

Attendance recorded only once.

Cancelled events cannot accept registrations.

Soft delete only.

Media handled by Media Module.

Registration history maintained.

---

# Integration

Integrate with

Authentication Module

User Module

Media Module

Payment Module

Notification Module

Do NOT implement Notification delivery.

---

# DTOs

Create

CreateEventRequest

UpdateEventRequest

EventRegistrationRequest

AttendanceRequest

FeedbackRequest

SearchEventRequest

EventResponse

EventSummaryResponse

EventDetailResponse

EventRegistrationResponse

EventAttendanceResponse

EventStatisticsResponse

---

# Search

Support

Title

Category

Organizer

Speaker

Location

Type

Status

Date

Keyword

Tags

Sorting

Pagination

Spring Specification

---

# Validation

Validate

Title

Category

Capacity

Venue

Schedule

Registration Period

Meeting Link

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

EventCreatedEvent

EventUpdatedEvent

EventPublishedEvent

EventRegistrationEvent

EventAttendanceEvent

EventCancelledEvent

EventCompletedEvent

FeedbackSubmittedEvent

---

# Security

Secure all APIs.

Use

@PreAuthorize

Examples

ROLE_ADMIN

ROLE_EVENT_ORGANIZER

ROLE_INSTRUCTOR

ROLE_USER

Only organizers and administrators may modify events.

---

# Logging

Log

Event Created

Event Updated

Registration

Attendance

Cancellation

Completion

Feedback

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

☑ No Notification implementation

Stop.

Wait for approval.

Next Module

20_NOTIFICATION.md