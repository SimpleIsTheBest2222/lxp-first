package com.potenup.lxp.common.jdbc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

// DB 접속 설정을 읽고 필수 값 유무를 검증하는 설정 객체다.
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
		// 실행 환경 변수 또는 시스템 프로퍼티에서 DB 접속 정보를 읽는다.
		return new DatabaseConfig(
			read("db.url", "DB_URL"),
			read("db.username", "DB_USERNAME"),
			read("db.password", "DB_PASSWORD")
		);
	}

	public static DatabaseConfig fromResource(String resourcePath) {
		// 테스트나 로컬 실행용으로 리소스 파일에서 설정을 읽을 수 있게 한다.
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
				// 빈 줄과 주석은 무시하고 key:value 형태만 읽는다.
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
		// 설정 파일 누락은 이후 단계에서 복구할 수 없으므로 즉시 실패시킨다.
		InputStream inputStream = DatabaseConfig.class.getResourceAsStream(resourcePath);
		if (inputStream == null) {
			throw new IllegalStateException("Config resource not found: " + resourcePath);
		}
		return inputStream;
	}

	private static String read(String propertyName, String envName) {
		// 시스템 프로퍼티를 우선하고, 없으면 환경 변수 값을 사용한다.
		String propertyValue = System.getProperty(propertyName);
		if (propertyValue != null && !propertyValue.isBlank()) {
			return propertyValue;
		}
		return System.getenv(envName);
	}

	private String requireValue(String value, String keyName) {
		// 필수 설정이 비어 있으면 애플리케이션 시작 단계에서 바로 막는다.
		if (value == null || value.isBlank()) {
			throw new IllegalStateException(keyName + " must be configured.");
		}
		return value;
	}
}
