package org.shagie.util.mapserializer;

import java.util.Map;

import static org.testng.Assert.*;

public class MapperTest {

    @org.testng.annotations.Test
    public void testToMap() throws Exception {

        class TestInner {
            int field1;
            String field2;
            public TestInner(int arg1, String arg2) {
                field1 = arg1;
                field2 = arg2;
            }
        }

        Mapper m = new Mapper();
        Map<String, Object> result = m.toMap(new TestInner(1,"foo"));

        assertTrue(result.containsKey("field1"));
        assertTrue(result.containsKey("field2"));
        assertEquals("foo", result.get("field2"));
        assertEquals(1, result.get("field1"));

    }
}