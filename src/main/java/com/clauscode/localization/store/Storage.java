package com.clauscode.localization.store;

import com.clauscode.localization.factory.Factory;

import java.util.List;
import java.util.Map;

/**
 * Used to access localization data.
 */
public interface Storage {
    /**
     * Creates a factory that extends the ability to interact with localized strings.
     * @param language Responsible for the selected localization language.
     */
    Factory buildFactory(String language);
    /**
     * Returns a string by id in the given language.
     * @param identifier Points to the ID of your row.
     * @param language Responsible for the selected localization language.
     */
    String getSingle(String identifier, String language);
    /**
     * Returns a map of strings and their identifiers in the given language.
     * @param language Responsible for the selected localization language.
     * @param identifiers Points to a list of row IDs you need to get.
     */
    Map<String, String> getGroup(String language, List<String> identifiers);
}
