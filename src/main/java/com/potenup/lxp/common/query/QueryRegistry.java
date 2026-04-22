package com.potenup.lxp.common.query;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class QueryRegistry {
    private final Map<String, String> queries;

    private QueryRegistry(Map<String, String> queries) {
        this.queries = queries;
    }

    public static QueryRegistry fromXmlResource(String resourcePath) {
        try (InputStream inputStream = QueryRegistry.class.getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IllegalStateException("Query resource not found: " + resourcePath);
            }

            Document document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(inputStream);
            NodeList queryNodes = document.getElementsByTagName("query");
            Map<String, String> queries = new HashMap<>();

            for (int index = 0; index < queryNodes.getLength(); index++) {
                Element element = (Element) queryNodes.item(index);
                String queryId = element.getAttribute("id");
                String sql = element.getTextContent().trim();
                queries.put(queryId, sql);
            }

            return new QueryRegistry(queries);
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to load query resource: " + resourcePath, exception);
        }
    }

    public String get(String queryId) {
        String query = queries.get(queryId);
        if (query == null || query.isBlank()) {
            throw new IllegalStateException("Query not found: " + queryId);
        }
        return query;
    }
}
