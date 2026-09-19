package com.academiabase.dao;

import com.academiabase.config.DatabaseConnection;
import com.academiabase.models.Enrollment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {

    private static final String INSERT_ENROLLMENT =
            "INSERT INTO Enrollments (student_id, course_id, semester) VALUES (?, ?, ?)";
    private static final String SELECT_BY_STUDENT =
            "SELECT e.enrollment_id, e.student_id, e.course_id, e.semester, "
          + "c.course_code, c.course_name "
          + "FROM Enrollments e JOIN Courses c ON e.course_id = c.course_id "
          + "WHERE e.student_id = ?";
    private static final String SELECT_BY_COURSE =
            "SELECT e.enrollment_id, e.student_id, e.course_id, e.semester, "
          + "s.first_name, s.last_name "
          + "FROM Enrollments e JOIN Students s ON e.student_id = s.student_id "
          + "WHERE e.course_id = ?";
    private static final String SELECT_ALL =
            "SELECT e.enrollment_id, e.student_id, e.course_id, e.semester, "
          + "s.first_name, s.last_name, c.course_code, c.course_name "
          + "FROM Enrollments e "
          + "JOIN Students s ON e.student_id = s.student_id "
          + "JOIN Courses c ON e.course_id = c.course_id";
    private static final String DELETE_ENROLLMENT =
            "DELETE FROM Enrollments WHERE enrollment_id = ?";
    private static final String CHECK_DUPLICATE =
            "SELECT COUNT(*) FROM Enrollments "
          + "WHERE student_id = ? AND course_id = ? AND semester = ?";

    public int enrollStudent(Enrollment enrollment) {
        if (isDuplicate(enrollment)) {
            System.err.println("[EnrollmentDAO] Duplicate: student "
                    + enrollment.getStudentId() + " is already enrolled in course "
                    + enrollment.getCourseId() + " for " + enrollment.getSemester());
            return -1;
        }
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(INSERT_ENROLLMENT, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, enrollment.getStudentId());
                pstmt.setInt(2, enrollment.getCourseId());
                pstmt.setString(3, enrollment.getSemester());
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    try (ResultSet keys = pstmt.getGeneratedKeys()) {
                        if (keys.next()) {
                            int newId = keys.getInt(1);
                            System.out.println("[EnrollmentDAO] Enrollment created with ID: " + newId);
                            return newId;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[EnrollmentDAO] Error enrolling student: " + e.getMessage());
        }
        return -1;
    }

    public List<String> getEnrollmentsByStudent(int studentId) {
        List<String> results = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_STUDENT)) {
                pstmt.setInt(1, studentId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        results.add(String.format("EnrollID=%d | %s - %s | Semester: %s",
                                rs.getInt("enrollment_id"),
                                rs.getString("course_code"),
                                rs.getString("course_name"),
                                rs.getString("semester")));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[EnrollmentDAO] Error fetching enrollments: " + e.getMessage());
        }
        return results;
    }

    public List<String> getEnrollmentsByCourse(int courseId) {
        List<String> results = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_COURSE)) {
                pstmt.setInt(1, courseId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        results.add(String.format("EnrollID=%d | Student: %s %s (ID=%d) | Semester: %s",
                                rs.getInt("enrollment_id"),
                                rs.getString("first_name"),
                                rs.getString("last_name"),
                                rs.getInt("student_id"),
                                rs.getString("semester")));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[EnrollmentDAO] Error fetching enrollments by course: " + e.getMessage());
        }
        return results;
    }

    public List<String> getAllEnrollments() {
        List<String> results = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    results.add(String.format("EnrollID=%d | %s %s -> %s (%s) | Semester: %s",
                            rs.getInt("enrollment_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("course_code"),
                            rs.getString("course_name"),
                            rs.getString("semester")));
                }
            }
        } catch (SQLException e) {
            System.err.println("[EnrollmentDAO] Error fetching all enrollments: " + e.getMessage());
        }
        return results;
    }

    public boolean deleteEnrollment(int enrollmentId) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(DELETE_ENROLLMENT)) {
                pstmt.setInt(1, enrollmentId);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("[EnrollmentDAO] Error deleting enrollment: " + e.getMessage());
        }
        return false;
    }

    private boolean isDuplicate(Enrollment enrollment) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(CHECK_DUPLICATE)) {
                pstmt.setInt(1, enrollment.getStudentId());
                pstmt.setInt(2, enrollment.getCourseId());
                pstmt.setString(3, enrollment.getSemester());
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("[EnrollmentDAO] Error checking duplicates: " + e.getMessage());
        }
        return false;
    }
}
