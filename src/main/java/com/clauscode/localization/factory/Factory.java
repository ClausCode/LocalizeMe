package com.clauscode.localization.factory;

import com.clauscode.localization.LocalizeString;
import com.clauscode.localization.group.GroupBuilder;

/**
 * Extends the ability to interact with localized strings.
 * Supports parameters.
 */
public interface Factory {
    /**
     * Changes localization language
     * @param language Responsible for the selected localization language.
     */
    Factory withLanguage(String language);
    /**
     * Sets the value of the message parameter
     * @param property Parameter name
     * @param value Parameter value
     */
    Factory withProperty(String property, Object value);
    /**
     * Creates a localized group.
     * The peculiarity of the group is a single request for all rows, which significantly reduces the delay
     * Supports parameters.
     */
    GroupBuilder createGroup();
    /**
     * Creates a localized string object.
     * Supports parameters.
     */
    LocalizeString getSingle(String identifier);
}
