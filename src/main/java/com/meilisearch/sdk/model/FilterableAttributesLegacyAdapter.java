package com.meilisearch.sdk.model;

import com.meilisearch.sdk.exceptions.GranularFilterableAttributesException;
import java.util.Arrays;

/**
 * Adapts between legacy String[] filterable attributes and the granular FilterableAttributesConfig
 * representation.
 */
public final class FilterableAttributesLegacyAdapter {

    private FilterableAttributesLegacyAdapter() {}

    /** Converts legacy attribute names to granular configs (null-safe). */
    public static FilterableAttributesConfig[] fromLegacyNames(String[] names) {
        if (names == null) {
            return null;
        }
        return Arrays.stream(names)
                .map(
                        name ->
                                name == null
                                        ? null
                                        : FilterableAttributesConfig.fromAttributeName(name))
                .toArray(FilterableAttributesConfig[]::new);
    }

    /**
     * Converts granular configs back to legacy attribute names when possible.
     *
     * <p>Throws {@link GranularFilterableAttributesException} if any config cannot be reduced to a
     * single name without losing granular details.
     */
    public static String[] toLegacyNamesOrThrow(FilterableAttributesConfig[] configs) {
        if (configs == null) {
            return null;
        }

        String[] names = new String[configs.length];
        for (int i = 0; i < configs.length; i++) {
            FilterableAttributesConfig config = configs[i];
            if (config == null) {
                names[i] = null;
                continue;
            }

            String[] patterns = config.getAttributePatterns();
            boolean isSimple =
                    config.getFeatures() == null
                            && patterns != null
                            && patterns.length == 1
                            && patterns[0] != null;

            if (!isSimple) {
                throw new GranularFilterableAttributesException(
                        "This index uses granular filterable attributes; please use the granular filterable attributes methods instead of the legacy String[] API.");
            }

            names[i] = patterns[0];
        }
        return names;
    }
}
