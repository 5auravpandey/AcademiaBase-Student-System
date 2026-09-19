package com.academiabase.dao;

import com.academiabase.config.DatabaseConnection;
import com.academiabase.models.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    private static final String INSERT_COURSE =
            "INSERT INTO Courses (course_code, course_name) VALUES (?, ?)";
    private static final String SELECT_BY_ID =
            "SELECT course_id, course_code, course_name FROM Courses WHERE course_id = ?";
    private static final String SELECT_BY_CODE =
            "SELECT course_id, course_code, course_name FROM Courses WHERE course_code = ?";
    private static final String SELECT_ALL =
            "SELECT course_id, course_code, course_name FROM Courses";
    private static final String UPDATE_COURSE =
            "UPDATE Courses SET course_code = ?, course_name = ? WHERE course_id = ?";
    private static final String DELETE_COURSE =
            "DELETE FROM Courses WHERE course_id = ?";

    public int addCourse(Course course) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(INSERT_COURSE, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, course.getCourseCode());
                pstmt.setString(2, course.getCourseName());
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    try (ResultSet keys = pstmt.getGeneratedKeys()) {
                        if (keys.next()) {
                            int newId = keys.getInt(1);
                            System.out.println("[CourseDAO] Course added with ID: " + newId);
                            return newId;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[CourseDAO] Error adding course: " + e.getMessage());
        }
        return -1;
    }

    public Course getCourseById(int courseId) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID)) {
                pstmt.setInt(1, courseId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) return extractCourse(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[CourseDAO] Error fetching course: " + e.getMessage());
        }
        return null;
    }

    public Course getCourseByCode(String courseCode) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_CODE)) {
                pstmt.setString(1, courseCode);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) return extractCourse(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[CourseDAO] Error fetching course by code: " + e.getMessage());
        }
        return null;
    }

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) courses.add(extractCourse(rs));
            }
        } catch (SQLException e) {
            System.err.println("[CourseDAO] Error fetching courses: " + e.getMessage());
        }
        return courses;
    }

    public boolean updateCourse(Course course) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_COURSE)) {
                pstmt.setString(1, course.getCourseCode());
                pstmt.setString(2, course.getCourseName());
                pstmt.setInt(3, course.getCourseId());
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("[CourseDAO] Error updating course: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteCourse(int courseId) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(DELETE_COURSE)) {
                pstmt.setInt(1, courseId);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("[CourseDAO] Error deleting course: " + e.getMessage());
        }
        return false;
    }

    private Course extractCourse(ResultSet rs) throws SQLException {
        return new Course(
                rs.getInt("course_id"),
                rs.getString("course_code"),
                rs.getString("course_name")
        );
    }
}
