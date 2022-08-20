package de.pinguparty.geopingu.worker.utils;

import de.pinguparty.geopingu.worker.domain.geocache.Geocache;

/**
 * Collection of utility methods for the validation of values.
 */
public class ValidationUtils {

    /**
     * Returns whether a given {@link String} is a syntactically valid geocache ID.
     *
     * @param geocacheId The geocache ID to check
     * @return True, if the geocache ID is syntactically valid; false otherwise
     */
    public static boolean isValidGeocacheID(String geocacheId) {
        //Sanity check
        if ((geocacheId == null) || geocacheId.isEmpty())
            return false;

        //Check geocache ID against the regex pattern
        return geocacheId.matches(Geocache.GEOCACHE_ID_PATTERN);
    }
}
