package com.potenup.lxp.common.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// DatabaseConfig를 바탕으로 JDBC Connection을 생성하는 공통 유틸리티다.
public class JdbcConnectionManager {
	private final DatabaseConfig properties;

	public JdbcConnectionManager(DatabaseConfig properties) {
		this.properties = properties;
	}

	public Connection getConnection() throws SQLException {
		// 매 호출마다 새 커넥션을 열고, 닫기는 호출한 쪽의 try-with-resources에 맡긴다.
		return DriverManager.getConnection(
			properties.getUrl(),
			properties.getUsername(),
			properties.getPassword()
		);
	}
}
