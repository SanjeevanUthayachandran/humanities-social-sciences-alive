# 20_NOTIFICATION.md

# HSSA Backend - Notification Management Module

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
- 20_EVENT.md

Implement ONLY the Notification Management module.

Do NOT implement Contact.

Do NOT implement Dashboard.

Stop after completing this module.

Wait for approval.

---

# Objective

Implement a complete enterprise-grade Notification Management module.

This module is a centralized notification service used by all other modules.

Every module must use this service instead of implementing its own notification logic.

Notifications must support multiple delivery channels.

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

Use ONLY the existing notification-related tables.

Do NOT create tables.

Do NOT rename tables.

Do NOT rename columns.

Do NOT modify relationships.

Generate backend code according to the existing schema.

---

# Package Structure

notification/

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

provider/

template/

event/

specification/

---

# Responsibilities

Implement

✔ Notification CRUD

✔ Notification Templates

✔ User Notifications

✔ Read / Unread Status

✔ Mark All Read

✔ Delete Notification

✔ Notification History

✔ Notification Preferences

✔ Notification Categories

✔ Notification Priority

✔ Notification Scheduling

✔ Notification Queue (Future Ready)

✔ Broadcast Notifications

✔ Search

✔ Filtering

✔ Pagination

✔ Statistics

✔ Archive

✔ Soft Delete

---

# Supported Channels

Support

In-App Notification

Email

SMS (Future)

Push Notification (Future)

WebSocket (Future)

The architecture must allow adding new providers without changing business logic.

---

# APIs

Create REST APIs.

Examples

GET

/api/v1/notifications

GET

/api/v1/notifications/{id}

POST

/api/v1/notifications

PATCH

/api/v1/notifications/{id}/read

PATCH

/api/v1/notifications/read-all

DELETE

/api/v1/notifications/{id}

GET

/api/v1/notifications/history

GET

/api/v1/notifications/search

GET

/api/v1/notifications/statistics

PUT

/api/v1/notifications/preferences

---

# Business Rules

Notifications belong to users.

Broadcast notifications are administrator only.

Deleted notifications remain in audit history.

Templates are reusable.

Notifications support scheduling.

Notification delivery status must be tracked.

Maintain notification history.

---

# Notification Types

Support

Authentication

Course

Lesson

Enrollment

Payment

Certificate

Research

Publication

Reading Club

Literary Corner

Workshop

Event

System

Announcement

Reminder

Future-ready for additional types.

---

# Integration

Integrate with

Authentication

Course

Enrollment

Payment

Certificate

Research

Publication

Workshop

Event

Reading Club

Literary Corner

Dashboard

Every module must publish events.

Notification module listens to those events.

---

# DTOs

Create

CreateNotificationRequest

NotificationPreferenceRequest

BroadcastNotificationRequest

NotificationResponse

NotificationSummaryResponse

NotificationHistoryResponse

NotificationStatisticsResponse

NotificationPreferenceResponse

---

# Search

Support

Recipient

Category

Priority

Read Status

Delivery Status

Date

Keyword

Sorting

Pagination

Spring Specification

---

# Validation

Validate

Recipient

Priority

Category

Message

Schedule Time

Template

Delivery Channel

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

Return DTOs only.

Never expose entities.

---

# Notification Providers

Design provider abstraction.

Example

NotificationProvider

EmailNotificationProvider

InAppNotificationProvider

SmsNotificationProvider (future)

PushNotificationProvider (future)

Business logic must never depend on provider implementations.

---

# Events

Create

NotificationCreatedEvent

NotificationReadEvent

NotificationDeletedEvent

NotificationSentEvent

NotificationFailedEvent

BroadcastNotificationEvent

---

# Security

Secure all APIs.

Use

@PreAuthorize

ROLE_ADMIN

ROLE_USER

Users access only their notifications.

Administrators may send broadcasts.

---

# Logging

Log

Notification Created

Notification Sent

Notification Read

Notification Deleted

Notification Failed

Broadcast Sent

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

✔ Provider Abstraction

✔ Notification Templates

✔ User Preferences

✔ Broadcast Support

✔ Events

✔ Search

✔ Filtering

✔ Pagination

✔ Security

Nothing else.

---

# Completion Checklist

Verify

☑ CRUD complete

☑ Read/Unread complete

☑ Preferences complete

☑ Broadcast complete

☑ Template support complete

☑ Provider abstraction complete

☑ Search complete

☑ Pagination complete

☑ Security complete

☑ Existing database respected

☑ No Contact implementation

Stop.

Wait for approval.

Next Module

21_CONTACT.md