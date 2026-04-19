# EduConnect Backend

Spring Boot REST API for the EduConnect Digital Education & E-Learning Governance System.

## Tech Stack
- Java 21, Spring Boot 3.2, Spring Security, Spring Data JPA
- MySQL, Hibernate, JWT Authentication
- Maven, Lombok, SLF4J, JUnit 5, Mockito

## Prerequisites
- Java 21+
- Maven 3.8+
- MySQL 8+

## Setup

### 1. Database
```sql
CREATE DATABASE educonnect_db;
```

### 2. Configure `application.properties`
```properties
spring.datasource.username=YOUR_USER
spring.datasource.password=YOUR_PASSWORD
```

### 3. Run
```bash
mvn spring-boot:run
```
Server starts on **http://localhost:8080**

## API Overview

| Module | Base Path |
|--------|-----------|
| Auth | `/api/auth` |
| Students | `/api/students` |
| Courses | `/api/courses` |
| Enrollments | `/api/enrollments` |
| Content | `/api/content` |
| Assessments | `/api/assessments` |
| Progress | `/api/progress` |
| Compliance | `/api/compliance` |
| Reports | `/api/reports` |
| Notifications | `/api/notifications` |
| Audit Logs | `/api/audit-logs` |

## Authentication
All protected endpoints require:  
`Authorization: Bearer <JWT_TOKEN>`

Obtain token via `POST /api/auth/login`

## Roles
`STUDENT` · `TEACHER` · `SCHOOL_ADMIN` · `PROGRAM_MANAGER` · `COMPLIANCE_OFFICER` · `GOVERNMENT_AUDITOR`

## Running Tests
```bash
mvn test
```
