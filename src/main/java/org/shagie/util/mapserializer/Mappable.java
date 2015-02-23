package org.shagie.util.mapserializer;

import java.util.Map;

public interface Mappable {
    Map<String, Object> toMap();
}
