<<<<<<< HEAD
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
=======
# AcademiaBase: Student Information System 🎓

Welcome to AcademiaBase! I built this Student Information System (SIS) as my BCA Minor Project to get hands-on experience with backend architecture, relational database design, and secure data handling.

Instead of just throwing data into tables, I focused heavily on the "Analysis and Design" phase. I mapped out the entity relationships, created a properly normalized MySQL database to prevent data redundancy, and connected it all securely using Core Java and JDBC. The system handles everything from student admissions to academic records, ensuring that Admins, Teachers, and Students only have access to what they are supposed to see.

## 🚀 What I Built (Key Features)
* **Role-Based Access Control (RBAC):** Distinct permissions so Admins, Teachers, and Students have tailored access.
* **Thoughtful Database Design:** A highly optimized, normalized MySQL schema.
* **Secure Backend Connectivity:** Safe and reliable database integration using Java Database Connectivity (JDBC) and PreparedStatements to prevent SQL injection.
* **Academic Workflows:** Tracks student admissions, course registrations, and grades efficiently.

## 🛠️ Technology Stack
* **Language:** Java (JDK 11+)
* **Database:** MySQL
* **Database API:** JDBC (Java Database Connectivity)
* **Build Tool:** Maven 
* **Design Tools:** Data Flow Diagrams (DFDs), Entity-Relationship (ER) Diagrams

## 📂 Project Structure

    ├── database/          # SQL scripts for schema creation
    ├── docs/              # System analysis documents (ER Diagrams, DFDs)
    ├── src/               # Java source code
    │   └── main/java/com/academiabase/
    │       ├── config/    # Database connection logic
    │       ├── models/    # Java objects (Student, Course, User)
    │       └── main/      # Application entry point
    ├── pom.xml            # Maven dependencies (JDBC driver)
    └── README.md          # Project documentation

## ⚙️ Setup Instructions
1. **Initialize Database:** Run `source database/schema.sql;` in your MySQL client.
2. **Configure Credentials:** Update the database username and password in `src/main/java/com/academiabase/config/DatabaseConnection.java`.
3. **Run Application:** Compile and run the application using your preferred IDE or Maven.

## 👨‍💻 Author
**Saurav Pandey**
* LinkedIn: [linkedin.com/in/5auravpandey](https://www.linkedin.com/in/5auravpandey)
* GitHub: [@5auravpandey](https://github.com/5auravpandey)
>>>>>>> 9910962c5b502a6acd4c242efa082f11dfcd81d4
