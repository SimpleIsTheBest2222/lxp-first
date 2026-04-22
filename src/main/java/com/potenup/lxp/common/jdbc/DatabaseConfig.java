package com.potenup.lxp.common.jdbc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class DatabaseConfig {
	private final String url;
	private final String username;
	private final String password;

	public DatabaseConfig(String url, String username, String password) {
		this.url = requireValue(url, "DB_URL");
		this.username = requireValue(username, "DB_USERNAME");
		this.password = requireValue(password, "DB_PASSWORD");
	}

	public static DatabaseConfig fromEnvironment() {
		return new DatabaseConfig(
			read("db.url", "DB_URL"),
			read("db.username", "DB_USERNAME"),
			read("db.password", "DB_PASSWORD")
		);
	}

	public static DatabaseConfig fromResource(String resourcePath) {
		Map<String, String> values = loadYamlLikeConfig(resourcePath);

		return new DatabaseConfig(
			values.get("db.url"),
			values.get("db.username"),
			values.get("db.password")
		);
	}

	public String getUrl() {
		return url;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	private static Map<String, String> loadYamlLikeConfig(String resourcePath) {
		try (InputStream inputStream = getResourceAsStream(resourcePath);
		     BufferedReader reader = new BufferedReader(
				     new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
			Map<String, String> values = new HashMap<>();
			String line;

			while ((line = reader.readLine()) != null) {
				String trimmedLine = line.trim();
				if (trimmedLine.isEmpty() || trimmedLine.startsWith("#")) {
					continue;
				}

				int separatorIndex = trimmedLine.indexOf(':');
				if (separatorIndex < 0) {
					continue;
				}

				String key = trimmedLine.substring(0, separatorIndex).trim();
				String value = trimmedLine.substring(separatorIndex + 1).trim();
				values.put(key, value);
			}
			return values;
		} catch (IOException exception) {
			throw new IllegalStateException("Failed to load config resource: " + resourcePath, exception);
		}
	}

	private static InputStream getResourceAsStream(String resourcePath) {
		InputStream inputStream = DatabaseConfig.class.getResourceAsStream(resourcePath);
		if (inputStream == null) {
			throw new IllegalStateException("Config resource not found: " + resourcePath);
		}
		return inputStream;
	}

	private static String read(String propertyName, String envName) {
		String propertyValue = System.getProperty(propertyName);
		if (propertyValue != null && !propertyValue.isBlank()) {
			return propertyValue;
		}
		return System.getenv(envName);
	}

	private String requireValue(String value, String keyName) {
		if (value == null || value.isBlank()) {
			throw new IllegalStateException(keyName + " must be configured.");
		}
		return value;
	}
}
