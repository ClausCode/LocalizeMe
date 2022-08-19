package com.clauscode.localization.group;

import com.clauscode.localization.LocalizeString;
import com.clauscode.localization.store.Storage;

import java.util.*;

public class LocalizeGroupImpl implements GroupBuilder, Group {
    private final Set<String> identifierSet = new HashSet<>();
    private final Map<String, String> resultMap = new HashMap<>();
    private final Map<String, Object> properties = new HashMap<>();
    private String language;
    private final Storage storage;

    public LocalizeGroupImpl(Storage storage, String language) {
        this.storage = storage;
        this.language = language;
    }

    public LocalizeGroupImpl(Storage storage, String language, Map<String, Object> properties) {
        this.storage = storage;
        this.language = language;
        this.properties.putAll(properties);
    }

    public GroupBuilder withProperty(String property, Object value) {
        properties.put(property, value);
        return this;
    }

    @Override
    public GroupBuilder withString(String identifier) {
        this.identifierSet.add(identifier);
        return this;
    }

    @Override
    public GroupBuilder withLanguage(String language) {
        this.language = language;
        return this;
    }

    @Override
    public Group build() {
        resultMap.putAll(storage.getGroup(language, new ArrayList<>(identifierSet)));
        return this;
    }

    @Override
    public LocalizeString get(String identifier) {
        return new LocalizeString(identifier, language, resultMap.get(identifier), properties);
    }
}
