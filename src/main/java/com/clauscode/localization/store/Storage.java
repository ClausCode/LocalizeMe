package com.clauscode.localization.store;

import java.util.List;
import java.util.Map;

public interface Store {
    String getSingle(String identifier, String language);
    Map<String, String> getGroup(String language, List<String> identifiers);
}
