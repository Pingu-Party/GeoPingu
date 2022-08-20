package de.pinguparty.geopingu.worker.domain.geocache;

import de.pinguparty.geopingu.bot.util.Emoji;
import de.pinguparty.geopingu.bot.util.MessageUtils;
import de.pinguparty.geopingu.worker.utils.ValidationUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;
import java.util.regex.Pattern;

@Document
public class Geocache {

    //Regex pattern for valid geocache IDs
    public static final String GEOCACHE_ID_PATTERN = "GC[A-Z0-9]+";

    @Id
    private String id;

    private GeocacheType type;

    private String name;

    private String owner;

    private double difficultyRating;

    private double terrainRating;

    private GeocacheSize size;

    private int favoritePoints;

    private String hint;

    private boolean isPremiumOnly = false;

    private boolean complete = false;

    public Geocache(String id) {
        setId(id);
    }

    public String getId() {
        return id;
    }

    private Geocache setId(String id) {
        //Sanity check
        if (!ValidationUtils.isValidGeocacheID(id)) throw new IllegalArgumentException("The geocache ID is not valid.");

        this.id = id;
        return this;
    }

    public GeocacheType getType() {
        return type;
    }

    public Geocache setType(GeocacheType type) {
        //Sanity check
        if (type == null) throw new IllegalArgumentException("The geocache type must not be null.");

        this.type = type;
        return this;
    }

    public String getName() {
        return name;
    }

    public Geocache setName(String name) {
        this.name = name;
        return this;
    }

    public String getOwner() {
        return owner;
    }

    public Geocache setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public double getDifficultyRating() {
        return difficultyRating;
    }

    public Geocache setDifficultyRating(double difficultyRating) {
        this.difficultyRating = difficultyRating;
        return this;
    }

    public double getTerrainRating() {
        return terrainRating;
    }

    public Geocache setTerrainRating(double terrainRating) {
        this.terrainRating = terrainRating;
        return this;
    }

    public GeocacheSize getSize() {
        return size;
    }

    public Geocache setSize(GeocacheSize size) {
        //Sanity check
        if (size == null) throw new IllegalArgumentException("The geocache size must not be null.");

        this.size = size;
        return this;
    }

    public int getFavoritePoints() {
        return favoritePoints;
    }

    public Geocache setFavoritePoints(int favoritePoints) {
        this.favoritePoints = favoritePoints;
        return this;
    }

    public String getHint() {
        return hint;
    }

    public Geocache setHint(String hint) {
        this.hint = hint;
        return this;
    }

    public boolean isPremiumOnly() {
        return isPremiumOnly;
    }

    public Geocache setPremiumOnly(boolean premiumOnly) {
        isPremiumOnly = premiumOnly;
        return this;
    }

    public boolean isComplete() {
        return complete;
    }

    public Geocache setComplete(boolean complete) {
        this.complete = complete;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Geocache geocache = (Geocache) o;
        return getId().equals(geocache.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return String.format("%s*%s*%s \\[%s]", Emoji.MONEY_BAG, MessageUtils.escapeMessageString(name), Emoji.MONEY_BAG, id) +
                String.format("\n*Type*: %s%s", type.getIcon(), type) +
                String.format("\n*Owner*: %s", (owner == null) ? "Unknown" : MessageUtils.escapeMessageString(owner)) +
                String.format("\n*Difficulty*: %s", (difficultyRating >= 1) ? Emoji.generateStarRating(difficultyRating, 5) : "Unknown") +
                String.format("\n*Terrain*:     %s", (terrainRating >= 1) ? Emoji.generateStarRating(terrainRating, 5) : "Unknown") +
                String.format("\n*Size*: %s", size.getName()) +
                String.format("\n*Favorite Points*: %s", (favoritePoints >= 0) ? Integer.toString(favoritePoints) : "Unknown") +
                String.format("\n*Hint*: %s", (hint == null) ? "None" : MessageUtils.escapeMessageString(hint));
    }
}
