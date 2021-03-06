/*
 * Copyright (c) 2015, Michael "shagie" Turner <turnmichael@gmail.com>
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package org.shagie.util.mapserializer;

import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
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

    @Test
    public void testIgnore() {
        class TestInner {
            int field1;

            @MapperIgnore
            String field2;

            public TestInner(int arg1, String arg2) {
                field1 = arg1;
                field2 = arg2;
            }
        }

        Mapper m = new Mapper();
        Map<String, Object> result = m.toMap(new TestInner(1, "foo"));

        assertTrue(result.containsKey("field1"));
        assertFalse(result.containsKey("field2"), "Ignored field found");
        assertEquals(result.get("field2"), null, "Ignored value found");
        assertEquals(result.get("field1"), 1);
    }


    @Test
    public void testRename() {
        class TestInner {
            int field1;

            @MapperRename("newName")
            String field2;

            public TestInner(int arg1, String arg2) {
                field1 = arg1;
                field2 = arg2;
            }
        }

        Mapper m = new Mapper();
        Map<String, Object> result = m.toMap(new TestInner(1, "foo"));

        assertTrue(result.containsKey("field1"));
        assertFalse(result.containsKey("field2"), "renamed field found");
        assertTrue(result.containsKey("newName"), "renamed field not found");
        assertEquals(result.get("newName"), "foo", "new name not found");
        assertEquals(result.get("field1"), 1);
    }

    @Test
    public void testMappable() {
        class TestInner implements Mappable {
            int field1;

            public TestInner(int arg1) { field1 = arg1; }

            public Map<String, Object> toMap() {
                Map<String, Object> rv = new HashMap<String, Object>();
                rv.put("foo", "bar");
                return rv;
            }
        }

        Mapper m= new Mapper();
        Map<String, Object> result = m.toMap(new TestInner(42));
        assertTrue(result.containsKey("foo"));
        assertFalse(result.containsKey("field1"));
        assertEquals("bar", result.get("foo"));
    }

}
