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

// JDBC를 사용해 instructor 테이블에 접근하는 저장소 구현체다.
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
			// 도메인 객체 값을 PreparedStatement에 바인딩해서 SQL 인젝션을 피한다.
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
				// 존재 여부만 필요하므로 첫 행이 있는지만 확인한다.
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
				// ResultSet 한 행을 도메인 객체로 변환해 목록에 담는다.
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
			int affectedRows = statement.executeUpdate();
			// 수정 대상이 없으면 호출 측에서 이상 상태를 알 수 있도록 예외로 처리한다.
			if (affectedRows == 0) {
				throw new IllegalStateException("No instructor updated for id: " + instructor.getId());
			}
		} catch (SQLException exception) {
			throw new IllegalStateException("Failed to update instructor.", exception);
		}
	}

	@Override
	public void deleteById(Long instructorId) {
		try (Connection connection = connectionManager.getConnection();
		     PreparedStatement statement = connection.prepareStatement(queryRegistry.get("instructor.deleteById"))) {
			statement.setLong(1, instructorId);
			int affectedRows = statement.executeUpdate();
			// 이미 삭제되었거나 없는 id라면 0건 반영되므로 실패로 본다.
			if (affectedRows == 0) {
				throw new IllegalStateException("No instructor deleted for id: " + instructorId);
			}
		} catch (SQLException exception) {
			throw new IllegalStateException("Failed to delete instructor.", exception);
		}
	}

	private Instructor mapInstructor(ResultSet resultSet) throws SQLException {
		// JDBC 결과를 도메인 규칙을 가진 Instructor 객체로 복원한다.
		return new Instructor(
			resultSet.getLong("id"),
			resultSet.getString("name"),
			resultSet.getString("introduction")
		);
	}
}
