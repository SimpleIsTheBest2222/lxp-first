package com.potenup.lxp.common.query;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

// XML에 정의된 SQL 문장을 id로 조회할 수 있게 보관하는 레지스트리다.
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
                // <query id="...">...</query> 형식을 읽어 메모리에 적재한다.
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
        // 잘못된 query id는 SQL 실행 전에 즉시 드러나도록 한다.
        if (query == null || query.isBlank()) {
            throw new IllegalStateException("Query not found: " + queryId);
        }
        return query;
    }
}
