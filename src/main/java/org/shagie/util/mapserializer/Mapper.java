package org.shagie.util.mapserializer;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class Mapper {
    public Map<String, Object> toMap(Object param) {
        if (param == null) {
            return null;
        }

        Map<String, Object> retVal = new LinkedHashMap<String, Object>();

        for (Field f : param.getClass().getDeclaredFields()) {
            boolean access = f.isAccessible();
            if (f.getType().isPrimitive()) {
                Object wrapped = null;
                Class<?> clazz = f.getType();

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

                    retVal.put(f.getName(), wrapped);
                } catch (IllegalAccessException e) {
                } finally {
                    f.setAccessible(access);
                }

            } else {
                try {
                    f.setAccessible(true);
                    retVal.put(f.getName(), f.get(param));
                } catch (IllegalAccessException e) {
                } finally {
                    f.setAccessible(access);
                }
            }
        }

        return retVal;
    }
}