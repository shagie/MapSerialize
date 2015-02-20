package org.shagie.util.mapserializer;

import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class MapperTest {

    @Test
    public void testToMap() {
        class TestInner {
            int field1;
            String field2;

            public TestInner(int arg1, String arg2) {
                field1 = arg1;
                field2 = arg2;
            }
        }

        Mapper m = new Mapper();
        Map<String, Object> result = m.toMap(new TestInner(1, "foo"));

        assertTrue(result.containsKey("field1"));
        assertTrue(result.containsKey("field2"));
        assertEquals(result.get("field2"), "foo");
        assertEquals(result.get("field1"), 1);
    }

    @Test
    public void testNull() {
        Mapper m = new Mapper();
        assertEquals(null, m.toMap(null));
    }
}