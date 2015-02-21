package org.shagie.util.mapserializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class may be subclassed to allow for special handling of particular bits of
 * functionality that occur during the process of converting the field to a map.
 */
public class Mapper {
    private static final Logger LOG = LoggerFactory.getLogger(Mapper.class);

    /**
     * Default constructor
     */
    public Mapper() {
    }

    /**
     * Convert the parameter (param) into a Map that represents it.
     * @param param Object to be converted to a map
     * @return Map of String (field name) to Object (field value)
     */
    public Map<String, Object> toMap(final Object param) {
        if (param == null) {
            return null;
        }

        Map<String, Object> retVal = new LinkedHashMap<String, Object>();

        for (Field f : param.getClass().getDeclaredFields()) {
            if(ignore(f)) { continue; }
            final String name = getName(f);
            if (f.getType().isPrimitive()) {
                retVal.put(name, extractPrimitive(param, f));
            } else {
                Object value = extractObject(param, f);

                // TODO transform?
                // TODO null handle?

                retVal.put(name, value);
            }
        }

        return retVal;
    }

    /**
     * Exgtract object associated with the field
     * @param param object with the fields
     * @param f field to be extracted
     * @return value associated with the field
     */
    protected Object extractObject(Object param, Field f) {
        Object value = null;
        final boolean access = f.isAccessible();
        try {
            f.setAccessible(true);
            value = f.get(param);
        } catch (IllegalAccessException e) {
            LOG.error("Illegal access for field " + f.getName(), e);
        } finally {
            f.setAccessible(access);
        }
        return value;
    }

    /**
     * Extract a primitive field
     * @param param object with the fields
     * @param f field to be extracted
     * @return Object of the type that wraps the primitive
     */
    protected Object extractPrimitive(Object param, Field f) {
        Object wrapped = null;
        final Class<?> clazz = f.getType();
        final boolean access = f.isAccessible();

        try {
            f.setAccessible(true);
            if (clazz.isInstance(boolean.class)) {
                wrapped = f.getBoolean(param);
            } else if (clazz.equals(byte.class)) {
                wrapped = f.getByte(param);
            } else if (clazz.equals(char.class)) {
                wrapped = f.getChar(param);
            } else if (clazz.equals(int.class)) {
                wrapped = f.getInt(param);
            } else if (clazz.equals(long.class)) {
                wrapped = f.getLong(param);
            } else if (clazz.equals(short.class)) {
                wrapped = f.getShort(param);
            } else if (clazz.equals(float.class)) {
                wrapped = f.getFloat(param);
            } else if (clazz.equals(double.class)) {
                wrapped = f.getDouble(param);
            }

        } catch (IllegalAccessException e) {
            LOG.error("Illegal access for field " + f.getName(), e);
        } finally {
            f.setAccessible(access);
        }
        return wrapped;
    }

    /**
     * Given a field, return the name of the field.
     * @param f field to be extracted
     * @return String field name
     */
    protected String getName(final Field f) {
        final MapperRename renameAnno = f.getAnnotation(MapperRename.class);
        final String name;
        if(renameAnno != null) {
            name = renameAnno.value();
        } else {
            name = f.getName();
        }
        return name;
    }

    /**
     * Determine if a given field should be ignored
     * @param f field to be ignored (maybe)
     * @return boolean ignore the field?
     */
    protected boolean ignore(final Field f) {
        return f.getAnnotation(MapperIgnore.class) != null;
    }

}
