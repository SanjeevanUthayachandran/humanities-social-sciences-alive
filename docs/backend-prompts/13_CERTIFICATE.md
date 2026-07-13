# 13_CERTIFICATE.md

# HSSA Backend - Certificate Management Module

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

Implement ONLY the Certificate Management module.

Do NOT implement Research.

Do NOT implement Publications.

Do NOT implement Reading Club.

Stop after completing this module.

Wait for approval.

---

# Objective

Implement a complete enterprise-grade Certificate Management module.

The module is responsible for generating, validating, managing, revoking and downloading digital certificates issued for completed courses.

Certificates must be secure, verifiable, and immutable once issued.

---

# Dependencies

This module depends on

✔ Authentication

✔ User Management

✔ Course

✔ Lesson

✔ Enrollment

✔ Payment

Reuse existing services.

Do not duplicate logic.

---

# Database

Read the existing database documentation.

Use ONLY the existing certificate-related tables.

Do NOT create tables.

Do NOT rename tables.

Do NOT rename columns.

Do NOT modify relationships.

Generate only backend code according to the existing schema.

---

# Package Structure

certificate/

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

generator/

verification/

event/

specification/

---

# Responsibilities

Implement

✔ Certificate Generation

✔ Certificate Issuing

✔ Certificate Download

✔ Certificate Preview

✔ Certificate Verification

✔ Certificate Validation

✔ Certificate Search

✔ Certificate Filtering

✔ Certificate Pagination

✔ Certificate Revocation

✔ Certificate Reissue

✔ Certificate History

✔ Certificate Statistics

✔ QR Code Generation

✔ Verification Code Generation

✔ PDF Generation

✔ Digital Signature Ready

✔ Template Support

✔ Certificate Expiration (Future Ready)

---

# APIs

Create REST APIs.

Examples

GET

/api/v1/certificates

GET

/api/v1/certificates/{id}

GET

/api/v1/certificates/verify/{certificateNumber}

POST

/api/v1/certificates/generate

POST

/api/v1/certificates/reissue

PATCH

/api/v1/certificates/{id}/revoke

GET

/api/v1/certificates/download/{id}

GET

/api/v1/certificates/preview/{id}

GET

/api/v1/certificates/history

GET

/api/v1/certificates/statistics

---

# Business Rules

Certificates are generated only after all eligibility requirements are satisfied.

Certificate generation must verify

Enrollment

Course Completion

Required Payment Status (if applicable)

Certificate Number must be globally unique.

Certificate Verification Code must be unique.

Issued certificates cannot be modified.

Revoked certificates remain searchable.

Certificate history must be maintained.

Support multiple certificate templates.

---

# Certificate Verification

Support

Certificate Number

Verification Code

QR Code

Public verification endpoint

Verification must return

Certificate Status

Student Name

Course

Issue Date

Validity

Without exposing sensitive information.

---

# Integration

Integrate with

Authentication Module

Course Module

Enrollment Module

Payment Module

Notification Module

Media Module

Do NOT implement email sending.

Notification module will handle delivery.

---

# DTOs

Create

GenerateCertificateRequest

ReissueCertificateRequest

RevokeCertificateRequest

CertificateVerificationRequest

CertificateResponse

CertificateSummaryResponse

CertificateDetailResponse

CertificateVerificationResponse

CertificateStatisticsResponse

CertificateHistoryResponse

---

# Search

Support

Student

Course

Certificate Number

Issue Date

Status

Instructor

Verification Status

Sorting

Pagination

Spring Specification

---

# Validation

Validate

Enrollment

Completion Status

Certificate Number

Verification Code

QR Data

Issue Date

Duplicate Certificates

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

# Generator

Design certificate generation using interfaces.

Example

CertificateGenerator

PdfCertificateGenerator

Future

HtmlCertificateGenerator

ImageCertificateGenerator

Business logic must never depend on a specific generator implementation.

---

# Events

Create

CertificateGeneratedEvent

CertificateIssuedEvent

CertificateDownloadedEvent

CertificateVerifiedEvent

CertificateRevokedEvent

CertificateReissuedEvent

---

# Security

Secure all APIs.

Use

@PreAuthorize

Examples

ROLE_ADMIN

ROLE_INSTRUCTOR

ROLE_USER

Public certificate verification endpoint is allowed.

Certificate download only for owner or authorized staff.

---

# Logging

Log

Certificate Generated

Certificate Issued

Certificate Downloaded

Certificate Verified

Certificate Revoked

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

✔ PDF Generator

✔ QR Code Support

✔ Verification Service

✔ Events

✔ Search

✔ Filtering

✔ Pagination

✔ Security

Nothing else.

---

# Completion Checklist

Verify

☑ Certificate Generation complete

☑ Certificate Verification complete

☑ Certificate Download complete

☑ QR Code support complete

☑ PDF Generation complete

☑ Search complete

☑ Pagination complete

☑ Security complete

☑ Existing database respected

☑ No Research implementation

Stop.

Wait for approval.

Next Module

14_RESEARCH.md