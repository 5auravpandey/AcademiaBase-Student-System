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
