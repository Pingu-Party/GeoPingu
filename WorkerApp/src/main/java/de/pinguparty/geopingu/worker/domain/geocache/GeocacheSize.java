package de.pinguparty.geopingu.worker.domain.geocache;

import java.util.Arrays;

public enum GeocacheSize {
    MICRO("Micro"),
    SMALL("Small"),
    REGULAR("Regular"),
    LARGE("Large"),
    OTHER("Other"),
    UNKNOWN("Unknown");

    private String name;

    GeocacheSize(String name){
        setName(name);
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public static GeocacheSize byName(String name){
        return Arrays.stream(values()).filter(s -> s.getName().equalsIgnoreCase(name)).findFirst().orElse(OTHER);
    }
}
