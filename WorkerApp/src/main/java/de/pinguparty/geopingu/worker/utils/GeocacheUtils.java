package de.pinguparty.geopingu.worker.utils;

import de.pinguparty.geopingu.worker.domain.geocache.Geocache;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeocacheUtils {

    private static final Pattern PATTERN_FIND_GEOCACHE_ID = Pattern.compile("^\\[?(" + Geocache.GEOCACHE_ID_PATTERN + ")]?.*", Pattern.DOTALL);

    public static String generateShortDescription(Geocache geocache) {
        //Sanity check
        if (geocache == null) throw new IllegalArgumentException("The geocache must not be null.");

        //Generate short description
        return String.format("[%s] %s", geocache.getId(), geocache.getName());
    }

    public static String findGeocacheId(String text) {
        Matcher matcher = PATTERN_FIND_GEOCACHE_ID.matcher(text);
        return matcher.find() ? matcher.group(1) : null;
    }
}
