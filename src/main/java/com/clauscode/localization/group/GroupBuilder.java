package com.clauscode.localization;

public interface GroupBuilder {
    GroupBuilder addItem(String identifier);
    Group build();
}
