package de.pinguparty.geopingu.bot.util;

/**
 * Enumeration of various emojis in unicode representation.
 */
public enum Emoji {
    RED_CROSS("\u274C"),
    CHECK_MARK("\u2705"),
    PENGUIN("\uD83D\uDC27"),
    GREEN_BOOK("\uD83D\uDCD7"),
    BLUE_BOOK("\uD83D\uDCD8"),
    ORANGE_BOOK("\uD83D\uDCD9"),
    BLACK_CIRCLE("\u26AB"),
    EARTH_AMERICA("\uD83C\uDF0E"),
    BEER("\uD83C\uDF7A"),
    ENVELOPE("\u2709"),
    CAMERA("\uD83D\uDCF7"),
    SMARTPHONE("\uD83D\uDCF1"),
    GHOST("\uD83D\uDC7B"),
    FULL_MOON("\uD83C\uDF15"),
    LAST_QUARTER_MOON("\uD83C\uDF17"),
    NEW_MOON("\uD83C\uDF11"),
    MONEY_BAG("\uD83D\uDCB0");

    private String unicode;

    /**
     * Creates a new {@link Emoji} from a given unicode representation.
     *
     * @param unicode The unicode representation
     */
    Emoji(String unicode) {
        setUnicode(unicode);
    }

    /**
     * Returns the unicode representation of the {@link Emoji}.
     *
     * @return The unicode representation
     */
    public String getUnicode() {
        return this.unicode;
    }

    /**
     * Sets the unicode representation of the {@link Emoji}.
     *
     * @param unicode The unicode representation to set
     */
    private void setUnicode(String unicode) {
        //Sanity check
        if ((unicode == null) || unicode.isEmpty())
            throw new IllegalArgumentException("The unicode representation must not be null or empty.");

        this.unicode = unicode;
    }

    /**
     * Generates a star rating string with {@link Emoji}s from a given rating and a maximum value.
     *
     * @param rating  The rating value in a range from 1 to the maximum value, in steps of 0.5
     * @param maximum The maximum value of the rating
     * @return The resulting star rating as string of {@link Emoji}s
     */
    public static String generateStarRating(double rating, double maximum) {
        //Check range
        if ((rating < 1) || (rating > maximum)) throw new IllegalArgumentException("The number is out of range.");

        //String builder for star rating
        StringBuilder starRatingBuilder = new StringBuilder();

        //Loop through the possible values to build the star rating string
        for (int i = 0; i < maximum; i++) {
            if ((i + 0.5) < rating) {
                //Full star
                starRatingBuilder.append(Emoji.FULL_MOON);
            } else if (i < rating) {
                //Half star
                starRatingBuilder.append(Emoji.LAST_QUARTER_MOON);
            } else {
                //No star
                starRatingBuilder.append(Emoji.NEW_MOON);
            }
        }

        //Build the rating
        return starRatingBuilder.toString();
    }

    /**
     * Returns the unicode representation of the {@link Emoji}.
     *
     * @return The unicode representation
     */
    @Override
    public String toString() {
        return this.getUnicode();
    }
}
