package com.potenup.lxp.common.jdbc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DatabaseConfigTest {
	@Test
	void fromResourceReadsDatabaseValuesFromConfigFile() {
		DatabaseConfig databaseConfig = DatabaseConfig.fromResource("/config.yml");

		assertEquals("jdbc:mysql://localhost:3306/WANTED_LMS", databaseConfig.getUrl());
		assertEquals("park", databaseConfig.getUsername());
		assertEquals("park", databaseConfig.getPassword());
	}
}
