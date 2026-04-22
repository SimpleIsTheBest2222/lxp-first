package com.potenup.lxp.course.repository;

import com.potenup.lxp.common.jdbc.JdbcConnectionManager;
import com.potenup.lxp.common.query.QueryRegistry;
import com.potenup.lxp.course.domain.Course;
import com.potenup.lxp.course.domain.CourseLevel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcCourseRepository implements CourseRepository {
    private final JdbcConnectionManager connectionManager;
    private final QueryRegistry queryRegistry;

    public JdbcCourseRepository(JdbcConnectionManager connectionManager, QueryRegistry queryRegistry) {
        this.connectionManager = connectionManager;
        this.queryRegistry = queryRegistry;
    }

    @Override
    public Long save(Course course) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     queryRegistry.get("course.insert"),
                     Statement.RETURN_GENERATED_KEYS
             )) {
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
    public List<Course> findAll() {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(queryRegistry.get("course.findAll"));
             ResultSet resultSet = statement.executeQuery()) {
            List<Course> courses = new ArrayList<>();
            while (resultSet.next()) {
                courses.add(mapCourse(resultSet));
            }
            return courses;
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to load courses.", exception);
        }
    }

    @Override
    public Optional<Course> findById(Long courseId) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(queryRegistry.get("course.findById"))) {
            statement.setLong(1, courseId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapCourse(resultSet));
                }
                return Optional.empty();
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to load course.", exception);
        }
    }

    @Override
    public void update(Course course) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(queryRegistry.get("course.update"))) {
            statement.setString(1, course.getTitle());
            statement.setString(2, course.getDescription());
            statement.setInt(3, course.getPrice());
            statement.setString(4, course.getLevel().name().toLowerCase());
            statement.setLong(5, course.getInstructorId());
            statement.setLong(6, course.getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to update course.", exception);
        }
    }

    @Override
    public void deleteById(Long courseId) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(queryRegistry.get("course.deleteById"))) {
            statement.setLong(1, courseId);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to delete course.", exception);
        }
    }

    @Override
    public boolean existsById(Long courseId) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(queryRegistry.get("course.existsById"))) {
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
             PreparedStatement statement = connection.prepareStatement(queryRegistry.get("course.existsByInstructorId"))) {
            statement.setLong(1, instructorId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to check course existence by instructor id.", exception);
        }
    }

    private Course mapCourse(ResultSet resultSet) throws SQLException {
        return new Course(
                resultSet.getLong("id"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                resultSet.getInt("price"),
                CourseLevel.valueOf(resultSet.getString("level").toUpperCase()),
                resultSet.getLong("instructor_id")
        );
    }
}
