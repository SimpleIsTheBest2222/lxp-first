package com.potenup.lxp.common.query;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class QueryRegistryTest {
    @Test
    void fromXmlResourceLoadsInstructorQueries() {
        QueryRegistry queryRegistry = QueryRegistry.fromXmlResource("/queries.xml");

        assertTrue(queryRegistry.get("instructor.insert").contains("INSERT INTO instructor"));
        assertTrue(queryRegistry.get("instructor.existsById").contains("FROM instructor"));
    }
}
