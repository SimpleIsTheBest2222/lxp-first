package com.potenup.lxp.course.repository;

import com.potenup.lxp.common.jdbc.JdbcConnectionManager;
import com.potenup.lxp.course.domain.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcCourseRepository implements CourseRepository {
    private static final String INSERT_SQL = """
            INSERT INTO course (title, description, price, level, instructor_id)
            VALUES (?, ?, ?, ?, ?)
            """;
    private static final String EXISTS_SQL = """
            SELECT 1
            FROM course
            WHERE id = ?
            """;
    private static final String EXISTS_BY_INSTRUCTOR_ID_SQL = """
            SELECT 1
            FROM course
            WHERE instructor_id = ?
            """;

    private final JdbcConnectionManager connectionManager;

    public JdbcCourseRepository(JdbcConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Long save(Course course) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, course.getTitle());
            statement.setString(2, course.getDescription());
            statement.setInt(3, course.getPrice());
            statement.setString(4, course.getLevel().name().toLowerCase());
            statement.setLong(5, course.getInstructorId());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
            }
            throw new IllegalStateException("Failed to retrieve generated course id.");
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to save course.", exception);
        }
    }

    @Override
    public boolean existsById(Long courseId) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(EXISTS_SQL)) {
            statement.setLong(1, courseId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to check course existence.", exception);
        }
    }

    @Override
    public boolean existsByInstructorId(Long instructorId) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(EXISTS_BY_INSTRUCTOR_ID_SQL)) {
            statement.setLong(1, instructorId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to check course existence by instructor id.", exception);
        }
    }
}
