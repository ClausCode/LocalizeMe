package com.clauscode.localization.group;

import com.clauscode.localization.LocalizeString;

/**
 * Provides access to translated strings.
 * All group parameters are inherited by strings.
 */
public interface Group {
    /**
     * Returns a localized string by its id.
     * All group parameters are inherited by strings.
     * @param identifier String id
     */
    LocalizeString get(String identifier);
}
