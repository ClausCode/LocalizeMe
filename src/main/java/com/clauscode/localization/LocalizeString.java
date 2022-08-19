package com.clauscode.localization;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LocalizeString {
    private final Map<String, Object> properties = new HashMap<>();

    private final String identifier;
    private final String language;
    private final String value;

    public LocalizeString(String identifier, String language, String value) {
        this.identifier = identifier;
        this.language = language;
        this.value = value;
    }

    public LocalizeString(String identifier, String language, String value, Map<String, Object> properties) {
        this.identifier = identifier;
        this.language = language;
        this.value = value;
        this.properties.putAll(properties);
    }

    public LocalizeString withProperty(String property, Object value) {
        properties.put(property, value);
        return this;
    }

    public String build() {
        String result = this.value;

        for(String key : properties.keySet()) {
            Object value = properties.get(key);
            result = result.replace(":" + key, value.toString());
        }
        return new String(result.getBytes(), StandardCharsets.UTF_8);
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getLanguage() {
        return language;
    }
}
