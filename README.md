# AcademiaBase — Student Information System

A CLI-based Student Information System built as a **BCA Minor Project** to showcase relational database design, secure data handling, and Role-Based Access Control (RBAC).

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Core Java 11 |
| Database | MySQL 9.7 |
| Connectivity | JDBC (mysql-connector-j 8.3.0) |
| Build Tool | Apache Maven |
| Architecture | DAO Design Pattern |
| Security | SHA-256 Password Hashing, PreparedStatements |

## Project Structure

```
AcademiaBase/
├── database/schema.sql                        # MySQL schema + seed data
├── pom.xml                                    # Maven build configuration
└── src/main/java/com/academiabase/
    ├── config/DatabaseConnection.java         # Singleton JDBC connection
    ├── models/                                # POJOs (User, Student, Course, Enrollment)
    ├── dao/                                   # Data Access Objects (CRUD + Auth)
    └── main/AcademiaBaseApp.java              # CLI entry point with RBAC menus
```

## Features

- **RBAC Login System** — Admin, Teacher, and Student roles with role-specific menus
- **SHA-256 Password Hashing** — Passwords are never stored in plain text
- **SQL Injection Prevention** — All queries use `PreparedStatement`
- **DAO Pattern** — Clean separation of data access logic from business logic
- **Relational Design** — 1:1 (User↔Student), Many-to-Many (Student↔Course via Enrollments)
- **Try-with-Resources** — Proper JDBC resource management to prevent memory leaks
- **Duplicate Enrollment Prevention** — Business logic validation in the DAO layer

## Database Schema (ER Diagram)

```
Users (1) ──── (1) Students (M) ──── (M) Courses
                        │                    │
                        └──── Enrollments ───┘
                              (Junction Table)
```

## Setup & Run

### Prerequisites
- Java 11+
- Apache Maven
- MySQL Server

### 1. Create the database
```sql
mysql -u root -p < database/schema.sql
```

### 2. Configure the connection
Update the password in `src/main/java/com/academiabase/config/DatabaseConnection.java`:
```java
private static final String PASSWORD = "your_mysql_password";
```

### 3. Compile & Run
```bash
mvn compile exec:java
```

### 4. Default Admin Login
| Username | Password |
|----------|----------|
| admin    | admin123 |

## License

This project is built for academic purposes (BCA Minor Project).
