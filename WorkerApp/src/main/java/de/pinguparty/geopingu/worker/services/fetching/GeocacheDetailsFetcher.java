package de.pinguparty.geopingu.worker.services.fetching;

import de.pinguparty.geopingu.worker.domain.geocache.Geocache;
import de.pinguparty.geopingu.worker.domain.geocache.GeocacheSize;
import de.pinguparty.geopingu.worker.domain.geocache.GeocacheType;
import de.pinguparty.geopingu.worker.utils.ValidationUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GeocacheDetailsFetcher {
    //URL of the geocache details page
    private static final String URL_GEOCACHE_DETAILS = "https://www.geocaching.com/geocache/%s";

    //Various regex patterns for extracting geocache details
    private static final Pattern PATTERN_GEOCACHE_TYPE = Pattern.compile("/app/ui-icons/sprites/cache-types\\.svg#icon-(\\d+)");
    private static final Pattern PATTERN_GEOCACHE_SIZE = Pattern.compile("/images/icons/container/([A-z]+).gif");

    public GeocacheDetailsFetcher() {

    }

    public Geocache fetchById(String geocacheId) throws IOException {
        //Sanity check
        if (!ValidationUtils.isValidGeocacheID(geocacheId))
            throw new IllegalArgumentException("The geocache ID is not valid.");

        //Retrieve the details page of the geocache
        Document document = Jsoup.connect(String.format(URL_GEOCACHE_DETAILS, geocacheId)).get();

        //Create new geocache object
        Geocache geocache = new Geocache(geocacheId);

        //Set geocache fields
        geocache.setType(extractType(document));
        geocache.setName(extractName(document));
        geocache.setOwner(extractOwner(document));
        geocache.setDifficultyRating(extractDifficultyRating(document));
        geocache.setTerrainRating(extractTerrainRating(document));
        geocache.setSize(extractSize(document));
        geocache.setFavoritePoints(extractFavoritePoints(document));
        geocache.setHint(extractHint(document));
        geocache.setPremiumOnly(isPremiumOnlyCache(document));
        geocache.setComplete(false);

        return geocache;
    }

    private GeocacheType extractType(Document document) {
        try {
            Element anchorElement = isPremiumOnlyCache(document) ? document.getElementsByClass("li__cache-icon").first() : document.getElementById("uxCacheImage");
            String iconLink = anchorElement.getElementsByTag("use").first().attr("xlink:href");
            Matcher matcher = PATTERN_GEOCACHE_TYPE.matcher(iconLink);
            int cacheTypeIndex = matcher.find() ? Integer.parseInt(matcher.group(1)) : -1;
            return GeocacheType.byIndex(cacheTypeIndex);
        } catch (Exception e) {
            return GeocacheType.UNKNOWN;
        }
    }

    private String extractName(Document document) {
        try {
            Element nameElement = isPremiumOnlyCache(document) ? document.getElementsByClass("heading-3").first() : document.getElementById("ctl00_ContentBody_CacheName");
            return nameElement.text();
        } catch (Exception e) {
            return "Unknown";
        }
    }

    private String extractOwner(Document document) {
        try {
            if (isPremiumOnlyCache(document)) {
                return document.getElementById("ctl00_ContentBody_uxCacheBy").text().replaceAll("A cache by ", "");
            }
            return document.getElementById("ctl00_ContentBody_mcd1").childNode(1).childNode(0).outerHtml();
        } catch (Exception e) {
            return null;
        }
    }

    private double extractDifficultyRating(Document document) {
        try {
            String difficultyString = isPremiumOnlyCache(document) ? document.getElementById("ctl00_ContentBody_lblDifficulty").nextElementSibling().text() : document.getElementById("ctl00_ContentBody_uxLegendScale").childNode(0).attr("alt").replace(" out of 5", "");
            return Double.parseDouble(difficultyString);
        } catch (Exception e) {
            return 0;
        }
    }

    private double extractTerrainRating(Document document) {
        try {
            String terrainString = isPremiumOnlyCache(document) ? document.getElementById("ctl00_ContentBody_lblTerrain").nextElementSibling().text() : document.getElementById("ctl00_ContentBody_Localize12").childNode(0).attr("alt").replace(" out of 5", "");
            return Double.parseDouble(terrainString);
        } catch (Exception e) {
            return 0;
        }
    }

    private GeocacheSize extractSize(Document document) {
        try {
            if (isPremiumOnlyCache(document)) {
                return GeocacheSize.byName(document.getElementById("ctl00_ContentBody_lblSize").nextElementSibling().text());
            }
            String iconLink = document.getElementById("ctl00_ContentBody_size").getElementsByTag("img").first().attr("src");
            Matcher matcher = PATTERN_GEOCACHE_SIZE.matcher(iconLink);
            return GeocacheSize.byName(matcher.find() ? matcher.group(1) : "");
        } catch (Exception e) {
            return GeocacheSize.UNKNOWN;
        }
    }

    private int extractFavoritePoints(Document document) {
        try {
            if (!isPremiumOnlyCache(document)) return -1;
            return Integer.parseInt(document.getElementById("ctl00_ContentBody_lblFavoritePoints").nextElementSibling().text());
        } catch (Exception e) {
            return -1;
        }
    }

    private String extractHint(Document document) {
        try {
            String hint = document.getElementById("div_hint").text();
            return hint.isEmpty() ? null : rot13(hint);
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isPremiumOnlyCache(Document document) {
        return document.getElementsByClass("premium-upgrade-widget").size() > 0;
    }

    private static String rot13(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c >= 'a' && c <= 'm') c += 13;
            else if (c >= 'A' && c <= 'M') c += 13;
            else if (c >= 'n' && c <= 'z') c -= 13;
            else if (c >= 'N' && c <= 'Z') c -= 13;
            sb.append(c);
        }
        return sb.toString();
    }
}
