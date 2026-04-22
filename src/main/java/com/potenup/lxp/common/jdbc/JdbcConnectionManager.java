package com.potenup.lxp.common.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnectionManager {
	private final DatabaseConfig properties;

	public JdbcConnectionManager(DatabaseConfig properties) {
		this.properties = properties;
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(
			properties.getUrl(),
			properties.getUsername(),
			properties.getPassword()
		);
	}
}
