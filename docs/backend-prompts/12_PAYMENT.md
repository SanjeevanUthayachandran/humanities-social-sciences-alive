# 12_PAYMENT.md

# HSSA Backend - Payment Management Module

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

Implement ONLY the Payment Management module.

Do NOT implement Certificate generation.

Do NOT implement Research.

Do NOT implement Publications.

Stop after completing this module.

Wait for approval.

---

# Objective

Implement a complete enterprise-grade Payment Management module.

This module is responsible for handling all financial transactions related to paid courses and platform services.

The design must be provider-independent.

The initial payment gateway is PayHere.

The architecture must support future gateways such as Stripe and PayPal without changing business logic.

---

# Dependencies

This module depends on

✔ Authentication

✔ User Management

✔ Course

✔ Enrollment

Reuse existing services.

Do not duplicate logic.

---

# Database

Read the existing database documentation.

Use ONLY the existing payment-related tables.

Do NOT create tables.

Do NOT rename tables.

Do NOT rename columns.

Do NOT modify relationships.

Generate only backend code according to the existing schema.

---

# Package Structure

payment/

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

gateway/

event/

specification/

---

# Responsibilities

Implement

✔ Payment Initialization

✔ Payment Verification

✔ Payment Confirmation

✔ Payment Cancellation

✔ Payment Refund

✔ Payment Status Tracking

✔ Payment History

✔ Payment Search

✔ Payment Filtering

✔ Payment Pagination

✔ Invoice Generation

✔ Receipt Generation

✔ Transaction Validation

✔ Payment Audit

✔ Webhook Processing

✔ Multiple Payment Gateway Support

✔ Currency Support

✔ Discount Support (future ready)

✔ Coupon Support (future ready)

---

# Payment Providers

Implement abstraction.

Current

PayHere

Future

Stripe

PayPal

Manual Payment

Use Strategy Pattern.

Business logic must never depend on a gateway implementation.

---

# APIs

Create REST APIs.

Examples

GET

/api/v1/payments

GET

/api/v1/payments/{id}

POST

/api/v1/payments/initiate

POST

/api/v1/payments/verify

POST

/api/v1/payments/webhook

PATCH

/api/v1/payments/{id}/cancel

PATCH

/api/v1/payments/{id}/refund

GET

/api/v1/payments/history

GET

/api/v1/payments/search

GET

/api/v1/payments/invoice/{id}

GET

/api/v1/payments/receipt/{id}

---

# Business Rules

Only authenticated users can initiate payments.

Only one successful payment per enrollment.

A payment belongs to one enrollment.

Cancelled payments cannot be completed.

Refunds require administrator authorization.

Invoices are immutable.

Receipts are generated only after successful payment.

Maintain complete transaction history.

---

# Integration

Integrate with

Authentication Module

Enrollment Module

Course Module

Notification Module

Do NOT implement certificate issuance.

Certificate module will validate payment status independently.

---

# DTOs

Create

PaymentRequest

PaymentVerificationRequest

RefundRequest

CancelPaymentRequest

WebhookRequest

PaymentResponse

PaymentSummaryResponse

PaymentHistoryResponse

InvoiceResponse

ReceiptResponse

PaymentStatisticsResponse

---

# Search

Support

Student

Course

Enrollment

Transaction ID

Gateway

Payment Status

Payment Date

Amount

Currency

Sorting

Pagination

Spring Specification

---

# Validation

Validate

Enrollment ID

Payment Amount

Currency

Gateway

Transaction Reference

Webhook Signature

Duplicate Transactions

Payment Status

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

PaymentInitiatedEvent

PaymentCompletedEvent

PaymentFailedEvent

PaymentCancelledEvent

PaymentRefundedEvent

InvoiceGeneratedEvent

ReceiptGeneratedEvent

---

# Security

Secure all APIs.

Use

@PreAuthorize

Examples

ROLE_USER

ROLE_ADMIN

Only payment owners or administrators may access payment details.

Webhook endpoints must be securely validated.

---

# Logging

Log

Payment Initiated

Payment Verified

Payment Completed

Payment Failed

Payment Refunded

Webhook Received

Never log

Card Details

CVV

Payment Tokens

Sensitive Gateway Information

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

✔ Gateway Abstraction

✔ PayHere Implementation

✔ Repositories

✔ DTOs

✔ Validators

✔ Specifications

✔ Events

✔ Invoice Generation

✔ Receipt Generation

✔ Webhook Handling

✔ Search

✔ Filtering

✔ Pagination

✔ Security

Nothing else.

---

# Completion Checklist

Verify

☑ Payment Initialization complete

☑ Payment Verification complete

☑ Payment History complete

☑ Refund workflow complete

☑ Invoice generation complete

☑ Receipt generation complete

☑ Gateway abstraction complete

☑ Webhook processing complete

☑ Security complete

☑ Existing database respected

☑ No Certificate implementation

Payment gateway implementations must be interface-based.

Example

PaymentGateway
    ├── PayHereGateway
    ├── StripeGateway (future)
    ├── PayPalGateway (future)

Business services must depend only on PaymentGateway, never on a specific provider implementation.

Stop.

Wait for approval.

Next Module

13_CERTIFICATE.md