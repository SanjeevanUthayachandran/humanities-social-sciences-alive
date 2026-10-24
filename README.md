# Humanities & Social Sciences Alive

<div align="center">

![Status](https://img.shields.io/badge/Status-Under%20Development-blue)
![Version](https://img.shields.io/badge/Version-1.0.0-green)
![License](https://img.shields.io/badge/License-All%20Rights%20Reserved-red)
![Frontend](https://img.shields.io/badge/Frontend-React%2019-61DAFB)
![Backend](https://img.shields.io/badge/Backend-Spring%20Boot%203-6DB33F)
![Database](https://img.shields.io/badge/Database-PostgreSQL-336791)
![Cloud](https://img.shields.io/badge/Cloud-AWS-orange)

*A modern Digital Education & Research Platform for the Humanities and Social Sciences.*

</div>

---

## 📖 Overview

**Humanities & Social Sciences Alive (HSSA)** is a comprehensive digital platform designed to support teaching, learning, research, academic publishing, and scholarly collaboration in the Humanities and Social Sciences.

The platform provides online courses, research repositories, publications, workshops, events, reading clubs, and literary resources through a secure, scalable, and user-friendly web application.

The project is being developed using modern software engineering principles, enterprise architecture, and cloud technologies to ensure long-term scalability and maintainability.

---

## 🎯 Vision

To foster creativity, critical thinking, and lifelong learning by providing an accessible digital platform dedicated to the Humanities and Social Sciences.

---

## 🚀 Mission

To empower students, educators, researchers, and lifelong learners through high-quality educational resources, collaborative research opportunities, and innovative digital learning experiences.

---

# ✨ Key Features

- Secure User Authentication
- Role-Based Access Control (RBAC)
- Course Management System
- Learning Management System (LMS)
- Research Repository
- Publication Management
- Reading Club
- Literary Corner
- Workshops & Events
- News & Announcements
- Student Dashboard
- Instructor Dashboard
- Administrator Dashboard
- Online Payments
- Course Enrollment
- Certificate Generation
- Notification System
- Search System
- Responsive User Interface

---

# 🏗 System Architecture

```
Users
    │
    ▼
React + Vite Frontend
    │
 REST API
    │
    ▼
Spring Boot Backend
    │
 ├──────────────┬───────────────┬──────────────┐
 ▼              ▼               ▼              ▼
PostgreSQL     AWS S3        Email Service   Payment Gateway
```

---

# 🛠 Technology Stack

## Frontend

- React 19
- TypeScript
- Vite
- Tailwind CSS
- shadcn/ui
- React Router
- Axios
- TanStack Query
- Zustand

## Backend

- Java 21
- Spring Boot 3
- Spring Security
- JWT Authentication
- Spring Data JPA
- Hibernate
- MapStruct
- Swagger OpenAPI

## Database

- PostgreSQL

## Cloud

- AWS EC2
- AWS RDS
- AWS S3
- AWS CloudFront

## DevOps

- Git
- GitHub
- GitHub Actions
- Nginx

---

# 📂 Project Structure

```
humanities-social-sciences-alive/

├── frontend/
├── backend/
├── database/
├── cloud/
├── infrastructure/
├── docs/
├── testing/
├── scripts/
└── README.md
```

---

# 👥 User Roles

- Guest
- Student
- Instructor
- Editor
- Administrator
- Super Administrator

---

# 🔒 Security Features

- JWT Authentication
- Role-Based Access Control
- Password Encryption (BCrypt)
- HTTPS
- Input Validation
- SQL Injection Protection
- XSS Protection
- CSRF Protection
- Secure File Uploads

---

# 📊 Development Status

| Module | Status |
|---------|--------|
| Project Planning | ✅ Completed |
| Requirements Engineering | ✅ Completed |
| System Architecture | ✅ Completed |
| Database Design | 🟡 In Progress |
| UI/UX Design | 🟡 In Progress |
| Backend Development | ⏳ Planned |
| Frontend Development | ⏳ Planned |
| Testing | ⏳ Planned |
| Deployment | ⏳ Planned |

---

# 🚀 Getting Started

## Clone Repository

```bash
git clone https://github.com/YOUR_USERNAME/humanities-social-sciences-alive.git
```

---

## Frontend

```powershell
cd frontend
Copy-Item .env.example .env
npm install
npm run dev
```

The frontend uses `VITE_API_BASE_URL` as the Spring Boot API root. For local development it defaults to `http://localhost:8080/api/v1`.

---

## Database

Start the local PostgreSQL service first (the installed service name may differ):

```powershell
Get-Service postgresql*
Start-Service postgresql-x64-17
```

For an existing HSSA database, apply the repeatable backend compatibility migration and then install the competition content, administrator allow-list, course artwork references, and payment policies:

```powershell
$env:DB_PASSWORD = Read-Host "PostgreSQL password" -MaskInput
$env:PGPASSWORD = $env:DB_PASSWORD
& "C:\Program Files\PostgreSQL\17\bin\psql.exe" -h localhost -U postgres -d hssa_db -v ON_ERROR_STOP=1 -f database/12_backend_compatibility.sql
.\database\apply_competition_seed.ps1
```

Do not put the PostgreSQL password in `application-dev.yml`; the backend now requires `DB_PASSWORD` from the shell environment.

For a new database, execute the SQL files in the order documented in `database/README.md`. Be aware that `database/01_database.sql` drops and recreates `hssa_db` and therefore deletes an existing development database.

---

## Backend

Open a second PowerShell terminal. The backend reads the database password from the environment; `.env` files are not loaded automatically by Spring Boot.

```powershell
cd backend
$env:DB_PASSWORD = Read-Host "PostgreSQL password" -MaskInput
mvn spring-boot:run
```

To deliver password-reset emails in development, configure the sending mailbox in the same PowerShell window before starting the backend:

```powershell
$env:MAIL_USERNAME = "your-sender@gmail.com"
$env:MAIL_FROM = $env:MAIL_USERNAME
$env:MAIL_PASSWORD = Read-Host "Gmail app password" -MaskInput
$env:FRONTEND_URL = "http://localhost:5173"
mvn spring-boot:run
```

For Gmail, enable 2-Step Verification and use a generated 16-digit App Password for `MAIL_PASSWORD`, not the normal Google account password. Keep it only in an environment variable; never commit it to `.env.example`, YAML, or Git. The backend intentionally returns a neutral success message for unknown email addresses so attackers cannot discover registered accounts. A real account receives a one-time link that expires after 30 minutes and opens the HSSA new-password page.

### Google sign-in

Create an OAuth 2.0 Client ID of type **Web application** in Google Cloud Console and add this development JavaScript origin:

```text
http://localhost:5173
```

Copy the generated client ID (it ends in `.apps.googleusercontent.com`) into `frontend/.env`:

```dotenv
VITE_GOOGLE_CLIENT_ID=your-client-id.apps.googleusercontent.com
```

Set the same client ID in the PowerShell terminal that starts the backend:

```powershell
$env:GOOGLE_CLIENT_ID = "your-client-id.apps.googleusercontent.com"
```

This popup-based flow does not need a redirect URI. Restart both the frontend and backend after changing these values. Never place the Google client secret in the frontend; this implementation only needs the public Web Client ID.

The API runs at `http://localhost:8080/api/v1` and Swagger UI is available at `http://localhost:8080/swagger-ui/index.html`.

Run the frontend in a third terminal after the backend is ready. Vite normally serves it at `http://localhost:5173`.

---

# 📅 Roadmap

- Authentication System
- Public Website
- Course Management
- Research Repository
- Publication System
- Payment Integration
- Student Dashboard
- Instructor Dashboard
- Admin Dashboard
- Analytics
- Mobile Application

---

# 🤝 Contributing

This repository is currently under active development.

Contributions are limited to authorized collaborators.

Please contact the repository owner before submitting any pull requests.

---

# 📄 License

**All Rights Reserved**

This repository and its contents are the intellectual property of the project owner.

Unauthorized copying, modification, redistribution, or commercial use of any part of this repository is prohibited without prior written permission.

© 2026 Humanities & Social Sciences Alive. All rights reserved.

---

# 👨‍💻 Author

**Sanjeevan Uthayachandran**

Computer Engineering Undergraduate  
Faculty of Engineering  
University of Peradeniya  
Sri Lanka

---

# ⭐ Acknowledgements

This project is inspired by leading educational platforms and modern software engineering practices, with the goal of providing an accessible and scalable digital ecosystem for Humanities and Social Sciences education and research.

---

<div align="center">

**Humanities & Social Sciences Alive**

*Education • Research • Innovation*

</div>
