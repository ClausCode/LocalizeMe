package com.clauscode.localization.group;

/**
 * Allows you to configure a group of localized strings.
 * Supports parameters.
 */
public interface GroupBuilder {
    /**
     * Adds a row to a group
     * @param identifier String id
     */
    GroupBuilder withString(String identifier);
    /**
     * Changes localization language
     * @param language Responsible for the selected localization language.
     */
    GroupBuilder withLanguage(String language);
    /**
     * Sets the value of the message parameter
     * @param property Parameter name
     * @param value Parameter value
     */
    GroupBuilder withProperty(String property, Object value);

    /**
     * Gathers a group containing all the requested strings in the selected language.
     * Cannot be changed later.
     */
    Group build();
}
