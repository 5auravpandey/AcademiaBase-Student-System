package com.academiabase.dao;

import com.academiabase.config.DatabaseConnection;
import com.academiabase.models.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    private static final String INSERT_STUDENT =
            "INSERT INTO Students (student_id, first_name, last_name) VALUES (?, ?, ?)";
    private static final String SELECT_BY_ID =
            "SELECT student_id, first_name, last_name FROM Students WHERE student_id = ?";
    private static final String SELECT_ALL =
            "SELECT student_id, first_name, last_name FROM Students";
    private static final String UPDATE_STUDENT =
            "UPDATE Students SET first_name = ?, last_name = ? WHERE student_id = ?";
    private static final String DELETE_STUDENT =
            "DELETE FROM Students WHERE student_id = ?";

    public boolean addStudent(Student student) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(INSERT_STUDENT)) {
                pstmt.setInt(1, student.getStudentId());
                pstmt.setString(2, student.getFirstName());
                pstmt.setString(3, student.getLastName());
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    System.out.println("[StudentDAO] Student added: "
                            + student.getFirstName() + " " + student.getLastName());
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("[StudentDAO] Error adding student: " + e.getMessage());
        }
        return false;
    }

    public Student getStudentById(int studentId) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID)) {
                pstmt.setInt(1, studentId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) return extractStudent(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[StudentDAO] Error fetching student: " + e.getMessage());
        }
        return null;
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) students.add(extractStudent(rs));
            }
        } catch (SQLException e) {
            System.err.println("[StudentDAO] Error fetching students: " + e.getMessage());
        }
        return students;
    }

    public boolean updateStudent(Student student) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_STUDENT)) {
                pstmt.setString(1, student.getFirstName());
                pstmt.setString(2, student.getLastName());
                pstmt.setInt(3, student.getStudentId());
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("[StudentDAO] Error updating student: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteStudent(int studentId) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(DELETE_STUDENT)) {
                pstmt.setInt(1, studentId);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("[StudentDAO] Error deleting student: " + e.getMessage());
        }
        return false;
    }

    private Student extractStudent(ResultSet rs) throws SQLException {
        return new Student(
                rs.getInt("student_id"),
                rs.getString("first_name"),
                rs.getString("last_name")
        );
    }
}
