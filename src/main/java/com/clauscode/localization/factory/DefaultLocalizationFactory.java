package com.clauscode.localization.factory;

import com.clauscode.localization.LocalizeString;
import com.clauscode.localization.group.GroupBuilder;
import com.clauscode.localization.group.LocalizeGroupImpl;
import com.clauscode.localization.store.Storage;

import java.util.HashMap;
import java.util.Map;

public class DefaultLocalizationFactory implements Factory {
    private final Map<String, Object> properties = new HashMap<>();

    private final Storage storage;
    private String language;

    public DefaultLocalizationFactory(Storage storage, String language) {
        this.storage = storage;
        this.language = language;
    }

    @Override
    public Factory withProperty(String property, Object value) {
        properties.put(property, value);
        return this;
    }

    @Override
    public Factory withLanguage(String language) {
        this.language = language;
        return this;
    }

    @Override
    public GroupBuilder createGroup() {
        return new LocalizeGroupImpl(storage, language, properties);
    }

    @Override
    public LocalizeString getSingle(String identifier) {
        return new LocalizeString(identifier, language, storage.getSingle(identifier, language));
    }
}
