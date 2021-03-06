package com.github.frcsty.bungeechannellistener.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class JsonUtils {

    public static String serializeJson(final String... objects) {
        return serializeJson(getReplacements(objects));
    }

    public static String serializeJson(final Map<String, String> objects) {
        final StringBuilder builder = new StringBuilder();

        builder.append("{");

        int index = objects.size();
        for (final String key : objects.keySet()) {
            builder.append("\"").append(key).append("\":\"").append(objects.get(key)).append("\"");

            index--;
            if (index > 0) {
                builder.append(",");
            }
        }

        builder.append("}");
        return builder.toString();
    }

    private static Map<String, String> getReplacements(final String... values) {
        final Map<String, String> replacements = new HashMap<>();
        for (int i = 0; i < values.length - 1; i += 2) {
            final String key = values[i];
            final String value = values[i + 1];
            replacements.put(key, value);
        }

        return replacements;
    }

    public static Map<String, String> getClassContents(final Object object, final Class<?> clazz) {
        final Map<String, String> result = new HashMap<>();

        final Method[] methods = clazz.getMethods();

        Arrays.stream(methods).forEach(method -> {
            try {
                if (method.getParameterCount() != 0) return;
                if (method.getReturnType() == Void.class) return;
                if (method.getReturnType() == void.class) return;

                final Object methodResult = method.invoke(object);

                if (methodResult == null) return;
                result.put(method.getName(), methodResult.toString());
            } catch (final IllegalAccessException | InvocationTargetException ex) {
                ex.printStackTrace();
            }
        });

        return result;
    }

}
