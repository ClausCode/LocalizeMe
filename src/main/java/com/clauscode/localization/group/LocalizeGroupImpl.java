package com.clauscode.localization.group;

import java.util.HashMap;
import java.util.Map;

public class GroupImpl implements GroupBuilder {
    private final Map<String, String> items = new HashMap<>();
    private String language;

    public GroupImpl(String language) {
        this.language = language;
    }

    @Override
    public GroupBuilder addItem(String identifier) {
        return null;
    }

    @Override
    public GroupImpl build() {
        return null;
    }
}
