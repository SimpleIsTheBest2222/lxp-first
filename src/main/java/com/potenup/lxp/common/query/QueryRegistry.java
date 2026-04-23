package com.potenup.lxp.common.query;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

// XML에 정의된 SQL 문장을 id로 조회할 수 있게 보관하는 레지스트리다.
public class QueryRegistry {
	private static final String QUERY_TAG = "query";
	private static final String QUERY_ID_ATTRIBUTE = "id";
	// XMLConstants에 없는 구현체별 feature는 상수로 캡슐화합니다.
	private final Map<String, String> queries;
	private static final String DISALLOW_DOCTYPE_DECL =
			"http://apache.org/xml/features/disallow-doctype-decl";

	private QueryRegistry(Map<String, String> queries) {
		this.queries = Map.copyOf(queries);
	}

	public static QueryRegistry fromXmlResource(String resourcePath) {
		try (InputStream inputStream = QueryRegistry.class.getResourceAsStream(resourcePath)) {
			if (inputStream == null) {
				throw new IllegalStateException("Query resource not found: " + resourcePath);
			}

			DocumentBuilderFactory factory = createSecureFactory();
			Document document = factory.newDocumentBuilder().parse(inputStream);
			NodeList queryNodes = document.getElementsByTagName(QUERY_TAG);
			Map<String, String> queries = new HashMap<>();

			for (int index = 0; index < queryNodes.getLength(); index++) {
				Element element = (Element) queryNodes.item(index);
				String queryId = element.getAttribute(QUERY_ID_ATTRIBUTE);
				String sql = element.getTextContent().trim();

				if (queryId.isBlank()) {
					throw new IllegalStateException("Query id must not be blank: " + resourcePath);
				}
				if (sql.isBlank()) {
					throw new IllegalStateException("Query sql must not be blank: " + queryId);
				}
				if (queries.containsKey(queryId)) {
					throw new IllegalStateException("Duplicate query id: " + queryId);
				}

				queries.put(queryId, sql);
			}

			return new QueryRegistry(queries);
		} catch (ParserConfigurationException | SAXException | IOException exception) {
			throw new IllegalStateException("Failed to load query resource: " + resourcePath, exception);
		}
	}

	private static DocumentBuilderFactory createSecureFactory() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		factory.setFeature(DISALLOW_DOCTYPE_DECL, true);
		factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
		factory.setXIncludeAware(false);
		factory.setExpandEntityReferences(false);

		return factory;
	}

	public String get(String queryId) {
		String query = queries.get(queryId);
		if (query == null) {
			throw new IllegalStateException("Query not found: " + queryId);
		}
		return query;
	}
}
