# 21_CONTACT.md

# HSSA Backend - Contact & Support Management Module

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
- 21_NOTIFICATION.md

Implement ONLY the Contact & Support module.

Do NOT implement Dashboard.

Do NOT implement Testing.

Stop after completing this module.

Wait for approval.

---

# Objective

Implement a complete enterprise-grade Contact & Support module.

The module manages communication between platform users and administrators.

Support

• Contact Us
• General Inquiries
• Technical Support
• Feedback
• Suggestions
• Complaint Handling
• FAQ Management
• Ticket Tracking

The module must support future integration with email systems and helpdesk software.

---

# Dependencies

This module depends on

✔ Authentication

✔ User Management

✔ Notification

✔ Media

Reuse existing services.

Do not duplicate logic.

---

# Database

Read the existing database documentation.

Use ONLY the existing contact-related tables.

Do NOT create tables.

Do NOT rename tables.

Do NOT rename columns.

Do NOT modify relationships.

Generate backend code according to the existing schema.

---

# Package Structure

contact/

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

✔ Contact Form

✔ Contact Messages

✔ Support Tickets

✔ Ticket Categories

✔ Ticket Priority

✔ Ticket Assignment

✔ Ticket Replies

✔ Ticket Status

✔ Ticket Resolution

✔ User Feedback

✔ Suggestions

✔ Complaint Management

✔ FAQ Management

✔ Search

✔ Filtering

✔ Pagination

✔ Statistics

✔ Archive

✔ Soft Delete

---

# APIs

Create REST APIs.

Examples

GET

/api/v1/contact/messages

GET

/api/v1/contact/messages/{id}

POST

/api/v1/contact/messages

PUT

/api/v1/contact/messages/{id}

PATCH

/api/v1/contact/messages/{id}

DELETE

/api/v1/contact/messages/{id}

POST

/api/v1/contact/tickets

PATCH

/api/v1/contact/tickets/{id}/assign

PATCH

/api/v1/contact/tickets/{id}/resolve

POST

/api/v1/contact/tickets/{id}/reply

GET

/api/v1/contact/faqs

POST

/api/v1/contact/faqs

GET

/api/v1/contact/statistics

---

# Business Rules

Authenticated users may create support tickets.

Guests may submit contact forms if allowed by system settings.

Only administrators and support staff may assign or resolve tickets.

Each ticket maintains complete history.

Messages are never permanently deleted.

Support attachments use Media Module.

---

# Integration

Integrate with

Authentication

User Management

Media

Notification

Do NOT implement Dashboard.

---

# DTOs

Create

ContactRequest

SupportTicketRequest

ReplyRequest

FeedbackRequest

SuggestionRequest

FAQRequest

SearchContactRequest

ContactResponse

SupportTicketResponse

FAQResponse

ContactStatisticsResponse

---

# Search

Support

Ticket Number

Status

Priority

Category

Requester

Assigned Staff

Date

Keyword

Sorting

Pagination

Spring Specification

---

# Validation

Validate

Email

Subject

Message

Priority

Category

Attachments

Duplicate Requests

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

# Events

Create

ContactSubmittedEvent

SupportTicketCreatedEvent

SupportTicketAssignedEvent

SupportTicketResolvedEvent

FeedbackSubmittedEvent

FAQCreatedEvent

---

# Security

Secure all APIs.

Use

@PreAuthorize

Examples

ROLE_ADMIN

ROLE_SUPPORT

ROLE_USER

Users may only access their own support requests.

Support staff may manage assigned tickets.

Administrators have full access.

---

# Logging

Log

Contact Submitted

Ticket Created

Ticket Assigned

Ticket Resolved

Feedback Submitted

FAQ Updated

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

✔ Ticket Management

✔ FAQ Management

✔ Events

✔ Search

✔ Filtering

✔ Pagination

✔ Security

✔ Notification Integration

Nothing else.

---

# Completion Checklist

Verify

☑ Contact forms complete

☑ Support tickets complete

☑ Ticket assignment complete

☑ Ticket resolution complete

☑ FAQ management complete

☑ Search complete

☑ Pagination complete

☑ Security complete

☑ Existing database respected

☑ No Dashboard implementation

Stop.

Wait for approval.

Next Module

22_DASHBOARD.md