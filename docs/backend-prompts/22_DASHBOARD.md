# 22_DASHBOARD.md

# HSSA Backend - Dashboard & Analytics Module

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
- 22_CONTACT.md

Implement ONLY the Dashboard & Analytics module.

Do NOT implement Testing.

Do NOT implement Deployment.

Stop after completing this module.

Wait for approval.

---

# Objective

Implement a complete enterprise-grade Dashboard & Analytics module.

The Dashboard is responsible for providing analytical and summarized information across the HSSA platform.

The Dashboard must NEVER duplicate business logic from other modules.

It should consume data through existing services and repositories.

The Dashboard is read-only.

---

# Dependencies

This module depends on ALL previous modules.

Reuse existing services.

Do not duplicate code.

---

# Database

Read the existing database documentation.

Do NOT create tables.

Do NOT rename tables.

Do NOT modify schema.

Use existing data.

---

# Package Structure

dashboard/

controller/

service/
    impl/

repository/

dto/
    request/
    response/

mapper/

validator/

exception/

analytics/

statistics/

report/

---

# Responsibilities

Implement

✔ Admin Dashboard

✔ Instructor Dashboard

✔ Student Dashboard

✔ Public Dashboard

✔ System Statistics

✔ User Statistics

✔ Course Statistics

✔ Lesson Statistics

✔ Enrollment Statistics

✔ Payment Statistics

✔ Certificate Statistics

✔ Research Statistics

✔ Publication Statistics

✔ Reading Club Statistics

✔ Literary Corner Statistics

✔ Workshop Statistics

✔ Event Statistics

✔ Notification Statistics

✔ Contact Statistics

✔ Revenue Statistics

✔ Activity Timeline

✔ Recent Activities

✔ System Health Summary

✔ Charts Data

✔ Dashboard Widgets

✔ Reports Summary

---

# APIs

Create REST APIs.

Examples

GET

/api/v1/dashboard/admin

GET

/api/v1/dashboard/instructor

GET

/api/v1/dashboard/student

GET

/api/v1/dashboard/public

GET

/api/v1/dashboard/statistics

GET

/api/v1/dashboard/reports

GET

/api/v1/dashboard/recent-activities

GET

/api/v1/dashboard/charts

GET

/api/v1/dashboard/system-health

GET

/api/v1/dashboard/revenue

---

# Business Rules

Dashboard is read-only.

Never modify business data.

Statistics must be generated from existing modules.

Support role-based dashboards.

Support configurable date ranges.

Support caching where appropriate.

Future-ready for real-time updates.

---

# Analytics

Generate analytics for

Users

Courses

Lessons

Enrollments

Payments

Certificates

Research

Publications

Reading Clubs

Literary Works

Workshops

Events

Notifications

Support

Daily

Weekly

Monthly

Yearly

Custom Date Range

---

# Reports

Support

User Reports

Course Reports

Revenue Reports

Enrollment Reports

Certificate Reports

Research Reports

Publication Reports

Workshop Reports

Event Reports

Reading Club Reports

Literary Reports

Contact Reports

System Reports

---

# DTOs

Create

DashboardResponse

AdminDashboardResponse

InstructorDashboardResponse

StudentDashboardResponse

StatisticsResponse

ChartResponse

ReportResponse

RevenueResponse

RecentActivityResponse

SystemHealthResponse

---

# Search

Support

Date Range

Report Type

Category

Status

Sorting

Pagination

---

# Validation

Validate

Date Range

Report Type

Role

Statistics Request

---

# Repository

Use Spring Data JPA.

Reuse repositories where appropriate.

Avoid duplicate queries.

Optimize for read performance.

---

# Service Layer

Business logic belongs only in services.

Controllers remain thin.

Return DTOs only.

Never expose entities.

---

# Caching

Prepare dashboard caching.

Use Spring Cache.

Future ready for Redis.

---

# Events

Dashboard should consume existing events.

Do NOT create business events.

Only analytics events if required.

---

# Security

Secure all APIs.

Use

@PreAuthorize

Examples

ROLE_ADMIN

ROLE_INSTRUCTOR

ROLE_USER

Each role should only access its own dashboard.

---

# Logging

Log

Dashboard Access

Report Generation

Statistics Requests

System Health Checks

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

✔ DTOs

✔ Analytics Services

✔ Statistics Services

✔ Report Services

✔ Dashboard Widgets

✔ Charts

✔ Caching

✔ Search

✔ Pagination

✔ Security

Nothing else.

---

# Completion Checklist

Verify

☑ Admin Dashboard complete

☑ Instructor Dashboard complete

☑ Student Dashboard complete

☑ Analytics complete

☑ Reports complete

☑ Charts complete

☑ Statistics complete

☑ Security complete

☑ Existing database respected

☑ Read-only implementation

Stop.

Wait for approval.

Next Module

23_TESTING.md