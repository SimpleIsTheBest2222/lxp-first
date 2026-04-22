package com.potenup.lxp.instructor.repository;

import com.potenup.lxp.common.jdbc.JdbcConnectionManager;
import com.potenup.lxp.common.query.QueryRegistry;
import com.potenup.lxp.instructor.domain.Instructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcInstructorRepository implements InstructorRepository {
	private final JdbcConnectionManager connectionManager;
	private final QueryRegistry queryRegistry;

	public JdbcInstructorRepository(JdbcConnectionManager connectionManager, QueryRegistry queryRegistry) {
		this.connectionManager = connectionManager;
		this.queryRegistry = queryRegistry;
	}

	@Override
	public Long save(Instructor instructor) {
		try (Connection connection = connectionManager.getConnection();
			 PreparedStatement statement = connection.prepareStatement(
					 queryRegistry.get("instructor.insert"),
					 Statement.RETURN_GENERATED_KEYS
			 )) {
			statement.setString(1, instructor.getName());
			statement.setString(2, instructor.getIntroduction());
			statement.executeUpdate();

			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					return generatedKeys.getLong(1);
				}
			}
			throw new IllegalStateException("Failed to retrieve generated instructor id.");
		} catch (SQLException exception) {
			throw new IllegalStateException("Failed to save instructor.", exception);
		}
	}

	@Override
	public boolean existsById(Long instructorId) {
		try (Connection connection = connectionManager.getConnection();
			 PreparedStatement statement = connection.prepareStatement(queryRegistry.get("instructor.existsById"))) {
			statement.setLong(1, instructorId);

			try (ResultSet resultSet = statement.executeQuery()) {
				return resultSet.next();
			}
		} catch (SQLException exception) {
			throw new IllegalStateException("Failed to check instructor existence.", exception);
		}
	}

	@Override
	public List<Instructor> findAll() {
		try (Connection connection = connectionManager.getConnection();
			 PreparedStatement statement = connection.prepareStatement(queryRegistry.get("instructor.findAll"));
			 ResultSet resultSet = statement.executeQuery()) {
			List<Instructor> instructors = new ArrayList<>();
			while (resultSet.next()) {
				instructors.add(mapInstructor(resultSet));
			}
			return instructors;
		} catch (SQLException exception) {
			throw new IllegalStateException("Failed to load instructors.", exception);
		}
	}

	@Override
	public Optional<Instructor> findById(Long instructorId) {
		try (Connection connection = connectionManager.getConnection();
			 PreparedStatement statement = connection.prepareStatement(queryRegistry.get("instructor.findById"))) {
			statement.setLong(1, instructorId);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return Optional.of(mapInstructor(resultSet));
				}
				return Optional.empty();
			}
		} catch (SQLException exception) {
			throw new IllegalStateException("Failed to load instructor.", exception);
		}
	}

	@Override
	public void update(Instructor instructor) {
		try (Connection connection = connectionManager.getConnection();
			 PreparedStatement statement = connection.prepareStatement(queryRegistry.get("instructor.update"))) {
			statement.setString(1, instructor.getName());
			statement.setString(2, instructor.getIntroduction());
			statement.setLong(3, instructor.getId());
			statement.executeUpdate();
		} catch (SQLException exception) {
			throw new IllegalStateException("Failed to update instructor.", exception);
		}
	}

	@Override
	public void deleteById(Long instructorId) {
		try (Connection connection = connectionManager.getConnection();
			 PreparedStatement statement = connection.prepareStatement(queryRegistry.get("instructor.deleteById"))) {
			statement.setLong(1, instructorId);
			statement.executeUpdate();
		} catch (SQLException exception) {
			throw new IllegalStateException("Failed to delete instructor.", exception);
		}
	}

	private Instructor mapInstructor(ResultSet resultSet) throws SQLException {
		return new Instructor(
				resultSet.getLong("id"),
				resultSet.getString("name"),
				resultSet.getString("introduction")
		);
	}
}
