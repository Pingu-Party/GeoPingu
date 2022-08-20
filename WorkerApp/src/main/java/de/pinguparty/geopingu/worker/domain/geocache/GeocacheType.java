package de.pinguparty.geopingu.worker.domain.geocache;

import de.pinguparty.geopingu.bot.util.Emoji;

import java.util.Arrays;

public enum GeocacheType {
    TRADITIONAL(2, "Traditional Cache", Emoji.GREEN_BOOK),
    MULTI(3, "Multi Cache", Emoji.ORANGE_BOOK),
    VIRTUAL(4, "Virtual Cache", Emoji.GHOST),
    LETTERBOX(5, "Letterbox Cache", Emoji.ENVELOPE),
    EVENT(6, "Event Cache", Emoji.BEER),
    MYSTERY(8, "Mystery Cache", Emoji.BLUE_BOOK),
    WEBCAM(11, "Webcam Cache", Emoji.CAMERA),
    EARTH(137, "Earth Cache", Emoji.EARTH_AMERICA),
    WHERIGO(1858, "Wherigo Cache", Emoji.SMARTPHONE),
    UNKNOWN(-1, "Unknown", Emoji.BLACK_CIRCLE);

    private int index;
    private String name;
    private Emoji icon;

    GeocacheType(int index, String name, Emoji icon) {
        setIndex(index);
        setName(name);
        setIcon(icon);
    }

    public int getIndex() {
        return index;
    }

    private void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public Emoji getIcon() {
        return icon;
    }

    private void setIcon(Emoji icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static GeocacheType byIndex(int index) {
        return Arrays.stream(values()).filter(t -> t.getIndex() == index).findFirst().orElse(UNKNOWN);
    }
}
