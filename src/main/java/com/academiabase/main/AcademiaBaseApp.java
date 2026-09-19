package com.academiabase.main;

import com.academiabase.dao.*;
import com.academiabase.models.*;
import com.academiabase.models.User.Role;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class AcademiaBaseApp {

    private static final String LINE   = "══════════════════════════════════════════════════";
    private static final String DASHES = "──────────────────────────────────────────────────";

    private static final UserDAO       userDAO       = new UserDAO();
    private static final StudentDAO    studentDAO    = new StudentDAO();
    private static final CourseDAO     courseDAO     = new CourseDAO();
    private static final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        printWelcome();
        boolean running = true;
        while (running) {
            System.out.println("\n" + LINE);
            System.out.println("  MAIN MENU");
            System.out.println(LINE);
            System.out.println("  1. Login");
            System.out.println("  2. Exit");
            System.out.println(DASHES);

            int choice = readInt("  Enter choice: ");
            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    running = false;
                    System.out.println("\n  Goodbye! Thank you for using AcademiaBase.");
                    System.out.println(LINE + "\n");
                    break;
                default:
                    System.out.println("  [!] Invalid choice. Please select 1 or 2.");
            }
        }
        scanner.close();
    }

    private static void login() {
        System.out.println("\n" + DASHES);
        System.out.println("  LOGIN");
        System.out.println(DASHES);

        System.out.print("  Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("  Password: ");
        String password = scanner.nextLine().trim();

        User user = userDAO.authenticateUser(username, password);
        if (user == null) {
            System.out.println("\n  [✗] Invalid username or password. Please try again.");
            return;
        }

        System.out.println("\n  [✓] Welcome, " + user.getUsername()
                + "! (Role: " + user.getRole() + ")");

        switch (user.getRole()) {
            case Admin:   adminMenu(user);   break;
            case Teacher: teacherMenu(user); break;
            case Student: studentMenu(user); break;
        }
    }

    private static void adminMenu(User admin) {
        boolean active = true;
        while (active) {
            System.out.println("\n" + LINE);
            System.out.println("  ADMIN DASHBOARD  [" + admin.getUsername() + "]");
            System.out.println(LINE);
            System.out.println("  1. Register New Student");
            System.out.println("  2. Add New Course");
            System.out.println("  3. View All Students");
            System.out.println("  4. View All Courses");
            System.out.println("  5. View All Enrollments");
            System.out.println("  6. View All Users");
            System.out.println("  7. Delete a User");
            System.out.println("  0. Logout");
            System.out.println(DASHES);

            int choice = readInt("  Enter choice: ");
            switch (choice) {
                case 1: adminRegisterStudent(); break;
                case 2: adminAddCourse();       break;
                case 3: viewAllStudents();      break;
                case 4: viewAllCourses();       break;
                case 5: viewAllEnrollments();   break;
                case 6: viewAllUsers();         break;
                case 7: adminDeleteUser();      break;
                case 0:
                    System.out.println("  Logging out...");
                    active = false;
                    break;
                default:
                    System.out.println("  [!] Invalid choice.");
            }
        }
    }

    private static void adminRegisterStudent() {
        System.out.println("\n" + DASHES);
        System.out.println("  REGISTER NEW STUDENT");
        System.out.println(DASHES);

        System.out.print("  Choose a username: ");
        String username = scanner.nextLine().trim();
        System.out.print("  Choose a password: ");
        String password = scanner.nextLine().trim();
        System.out.print("  First name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("  Last name:  ");
        String lastName = scanner.nextLine().trim();

        int userId = userDAO.registerUser(username, password, Role.Student);
        if (userId == -1) {
            System.out.println("  [✗] Failed to create user account. Username may already exist.");
            return;
        }

        Student student = new Student(userId, firstName, lastName);
        boolean success = studentDAO.addStudent(student);

        if (success) {
            System.out.println("  [✓] Student registered successfully!");
            System.out.println("      User ID / Student ID: " + userId);
            System.out.println("      Username: " + username);
            System.out.println("      Name: " + firstName + " " + lastName);
        } else {
            System.out.println("  [✗] User created but student profile failed. Contact admin.");
        }
    }

    private static void adminAddCourse() {
        System.out.println("\n" + DASHES);
        System.out.println("  ADD NEW COURSE");
        System.out.println(DASHES);

        System.out.print("  Course code (e.g. CS101): ");
        String code = scanner.nextLine().trim().toUpperCase();
        System.out.print("  Course name: ");
        String name = scanner.nextLine().trim();

        Course course = new Course(0, code, name);
        int courseId = courseDAO.addCourse(course);

        if (courseId != -1) {
            System.out.println("  [✓] Course added! ID: " + courseId);
        } else {
            System.out.println("  [✗] Failed to add course. Code may already exist.");
        }
    }

    private static void adminDeleteUser() {
        System.out.println("\n" + DASHES);
        System.out.println("  DELETE USER");
        System.out.println(DASHES);

        viewAllUsers();
        int userId = readInt("  Enter User ID to delete (0 to cancel): ");
        if (userId == 0) return;

        System.out.print("  Are you sure? This will cascade to student/enrollment data (y/n): ");
        String confirm = scanner.nextLine().trim();

        if (confirm.equalsIgnoreCase("y")) {
            boolean deleted = userDAO.deleteUser(userId);
            System.out.println(deleted
                    ? "  [✓] User deleted successfully."
                    : "  [✗] User not found or deletion failed.");
        } else {
            System.out.println("  Cancelled.");
        }
    }

    private static void teacherMenu(User teacher) {
        boolean active = true;
        while (active) {
            System.out.println("\n" + LINE);
            System.out.println("  TEACHER DASHBOARD  [" + teacher.getUsername() + "]");
            System.out.println(LINE);
            System.out.println("  1. View All Courses");
            System.out.println("  2. View Students in a Course");
            System.out.println("  3. View All Enrollments");
            System.out.println("  4. Grading (Coming Soon)");
            System.out.println("  0. Logout");
            System.out.println(DASHES);

            int choice = readInt("  Enter choice: ");
            switch (choice) {
                case 1: viewAllCourses();              break;
                case 2: teacherViewStudentsInCourse(); break;
                case 3: viewAllEnrollments();          break;
                case 4:
                    System.out.println("  [i] Grading module is under development.");
                    break;
                case 0:
                    System.out.println("  Logging out...");
                    active = false;
                    break;
                default:
                    System.out.println("  [!] Invalid choice.");
            }
        }
    }

    private static void teacherViewStudentsInCourse() {
        System.out.println("\n" + DASHES);
        System.out.println("  STUDENTS IN A COURSE");
        System.out.println(DASHES);

        viewAllCourses();
        int courseId = readInt("  Enter Course ID: ");

        List<String> enrollments = enrollmentDAO.getEnrollmentsByCourse(courseId);
        if (enrollments.isEmpty()) {
            System.out.println("  No students enrolled in this course.");
        } else {
            System.out.println("\n  Enrolled students:");
            for (String e : enrollments) System.out.println("    " + e);
        }
    }

    private static void studentMenu(User studentUser) {
        boolean active = true;
        while (active) {
            System.out.println("\n" + LINE);
            System.out.println("  STUDENT DASHBOARD  [" + studentUser.getUsername() + "]");
            System.out.println(LINE);
            System.out.println("  1. View Available Courses");
            System.out.println("  2. Enroll in a Course");
            System.out.println("  3. View My Enrollments");
            System.out.println("  0. Logout");
            System.out.println(DASHES);

            int choice = readInt("  Enter choice: ");
            switch (choice) {
                case 1: viewAllCourses();                               break;
                case 2: studentEnroll(studentUser.getUserId());         break;
                case 3: studentViewEnrollments(studentUser.getUserId()); break;
                case 0:
                    System.out.println("  Logging out...");
                    active = false;
                    break;
                default:
                    System.out.println("  [!] Invalid choice.");
            }
        }
    }

    private static void studentEnroll(int studentId) {
        System.out.println("\n" + DASHES);
        System.out.println("  COURSE ENROLLMENT");
        System.out.println(DASHES);

        viewAllCourses();
        int courseId = readInt("  Enter Course ID to enroll in: ");

        System.out.print("  Semester (e.g. Fall 2026): ");
        String semester = scanner.nextLine().trim();

        Enrollment enrollment = new Enrollment(0, studentId, courseId, semester);
        int enrollId = enrollmentDAO.enrollStudent(enrollment);

        if (enrollId != -1) {
            System.out.println("  [✓] Enrolled successfully! Enrollment ID: " + enrollId);
        } else {
            System.out.println("  [✗] Enrollment failed. You may already be enrolled.");
        }
    }

    private static void studentViewEnrollments(int studentId) {
        System.out.println("\n" + DASHES);
        System.out.println("  MY ENROLLMENTS");
        System.out.println(DASHES);

        List<String> enrollments = enrollmentDAO.getEnrollmentsByStudent(studentId);
        if (enrollments.isEmpty()) {
            System.out.println("  You are not enrolled in any courses yet.");
        } else {
            for (String e : enrollments) System.out.println("    " + e);
        }
    }

    private static void viewAllStudents() {
        System.out.println("\n" + DASHES);
        System.out.println("  ALL STUDENTS");
        System.out.println(DASHES);

        List<Student> students = studentDAO.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("  No students registered.");
        } else {
            System.out.printf("  %-8s %-20s %-20s%n", "ID", "First Name", "Last Name");
            System.out.println("  " + DASHES.substring(2));
            for (Student s : students) {
                System.out.printf("  %-8d %-20s %-20s%n",
                        s.getStudentId(), s.getFirstName(), s.getLastName());
            }
        }
    }

    private static void viewAllCourses() {
        System.out.println("\n" + DASHES);
        System.out.println("  ALL COURSES");
        System.out.println(DASHES);

        List<Course> courses = courseDAO.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("  No courses available.");
        } else {
            System.out.printf("  %-8s %-10s %-40s%n", "ID", "Code", "Course Name");
            System.out.println("  " + DASHES.substring(2));
            for (Course c : courses) {
                System.out.printf("  %-8d %-10s %-40s%n",
                        c.getCourseId(), c.getCourseCode(), c.getCourseName());
            }
        }
    }

    private static void viewAllEnrollments() {
        System.out.println("\n" + DASHES);
        System.out.println("  ALL ENROLLMENTS");
        System.out.println(DASHES);

        List<String> enrollments = enrollmentDAO.getAllEnrollments();
        if (enrollments.isEmpty()) {
            System.out.println("  No enrollments found.");
        } else {
            for (String e : enrollments) System.out.println("    " + e);
        }
    }

    private static void viewAllUsers() {
        System.out.println("\n" + DASHES);
        System.out.println("  ALL USERS");
        System.out.println(DASHES);

        List<User> users = userDAO.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("  No users found.");
        } else {
            System.out.printf("  %-8s %-20s %-10s%n", "ID", "Username", "Role");
            System.out.println("  " + DASHES.substring(2));
            for (User u : users) {
                System.out.printf("  %-8d %-20s %-10s%n",
                        u.getUserId(), u.getUsername(), u.getRole());
            }
        }
    }

    private static int readInt(String prompt) {
        System.out.print(prompt);
        try {
            int value = scanner.nextInt();
            scanner.nextLine();
            return value;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println("  [!] Please enter a valid number.");
            return -1;
        }
    }

    private static void printWelcome() {
        System.out.println();
        System.out.println(LINE);
        System.out.println("     _                _            _       ____                 ");
        System.out.println("    / \\   ___ __ _  __| | ___ _ __ (_) __ _| __ )  __ _ ___  ___ ");
        System.out.println("   / _ \\ / __/ _` |/ _` |/ _ \\ '_ \\| |/ _` |  _ \\ / _` / __|/ _ \\");
        System.out.println("  / ___ \\ (_| (_| | (_| |  __/ | | | | (_| | |_) | (_| \\__ \\  __/");
        System.out.println(" /_/   \\_\\___\\__,_|\\__,_|\\___|_| |_|_|\\__,_|____/ \\__,_|___/\\___|");
        System.out.println();
        System.out.println("  Student Information System  |  v1.0  |  BCA Minor Project");
        System.out.println(LINE);
    }
}
