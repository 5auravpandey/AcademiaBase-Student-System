# AcademiaBase: Student Information System 🎓

Welcome to AcademiaBase! I built this CLI-based Student Information System (SIS) as my **BCA Minor Project** to get hands-on experience with backend architecture, relational database design, secure data handling, and Role-Based Access Control (RBAC).

Instead of just throwing data into tables, I focused heavily on the "Analysis and Design" phase. I mapped out the entity relationships, created a properly normalized MySQL database to prevent data redundancy, and connected it all securely using Core Java and JDBC. The system handles everything from student admissions to academic records, ensuring that Admins, Teachers, and Students only have access to what they are supposed to see.

## 🚀 What I Built (Key Features)

* **Role-Based Access Control (RBAC):** Admin, Teacher, and Student roles with tailored, role-specific menus.
* **Thoughtful Database Design:** A highly optimized, normalized MySQL schema handling 1:1 (User↔Student) and Many-to-Many (Student↔Course via Enrollments) relationships.
* **Secure Backend Connectivity:** Passwords are never stored in plain text (SHA-256 Hashing), and all database queries use `PreparedStatement` to prevent SQL injection.
* **DAO Pattern:** Clean separation of data access logic from business logic.
* **Robust Resource Management:** Uses Try-with-Resources for proper JDBC resource management to prevent memory leaks.
* **Duplicate Enrollment Prevention:** Business logic validation handled securely in the DAO layer.

## 📸 System Previews

Here is a look at the AcademiaBase CLI in action:

**1. Main Menu Initialization**  
![Main Menu](assets/image%201.png)

**2. Admin Dashboard & Login**  
![Admin Dashboard](assets/image%202.png)

**3. Viewing All Students**  
![View Students](assets/image%203.png)

**4. Student Dashboard & Registration**  
![Student Dashboard](assets/image%204.png)

## 🛠️ Technology Stack

| Layer | Technology |
|-------|-----------|
| **Language** | Core Java 11+ |
| **Database** | MySQL 9.7 |
| **Connectivity** | JDBC (mysql-connector-j 8.3.0) |
| **Build Tool** | Apache Maven |
| **Architecture** | DAO Design Pattern |
| **Security** | SHA-256 Password Hashing, PreparedStatements |
| **Design Tools**| Data Flow Diagrams (DFDs), Entity-Relationship (ER) Diagrams |

## 📊 Database Schema (ER Diagram)

```text
Users (1) ──── (1) Students (M) ──── (M) Courses
                        │                    │
                        └──── Enrollments ───┘
                              (Junction Table)
```

## 📂 Project Structure

```text
AcademiaBase/
├── database/schema.sql                        # MySQL schema + seed data
├── docs/                                      # System analysis documents (ER Diagrams, DFDs)
├── pom.xml                                    # Maven build configuration
└── src/main/java/com/academiabase/
    ├── config/DatabaseConnection.java         # Singleton JDBC connection
    ├── models/                                # POJOs (User, Student, Course, Enrollment)
    ├── dao/                                   # Data Access Objects (CRUD + Auth)
    └── main/AcademiaBaseApp.java              # CLI entry point with RBAC menus
```

## ⚙️ Setup Instructions

### Prerequisites
- Java 11+
- Apache Maven
- MySQL Server

### 1. Initialize the Database
Run the schema script in your MySQL client to build the tables:
```sql
mysql -u root -p < database/schema.sql
```
*(Alternatively, run `source database/schema.sql;` from within the MySQL prompt).*

### 2. Configure Credentials
Update the database username and password in `src/main/java/com/academiabase/config/DatabaseConnection.java`:
```java
private static final String PASSWORD = "your_mysql_password";
```

### 3. Compile & Run
Compile and run the application using Maven:
```bash
mvn compile exec:java
```

### 4. Default Admin Login
| Username | Password |
|----------|----------|
| admin    | admin123 |

## 👨‍💻 Author

**Saurav Pandey**
* LinkedIn: [linkedin.com/in/5auravpandey](https://www.linkedin.com/in/5auravpandey)
* GitHub: [@5auravpandey](https://github.com/5auravpandey)

## 📄 License
This project is built for academic purposes (BCA Minor Project).
