package de.pinguparty.geopingu.worker.domain;

import de.pinguparty.geopingu.worker.domain.formula.Formula;
import de.pinguparty.geopingu.worker.domain.geocache.Geocache;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
public class GeocacheUserNote {
    @Id
    private String id;

    @Indexed
    private long chatId;

    @DBRef
    private Geocache geocache;

    private GeocacheUserStatus status = GeocacheUserStatus.OPEN;

    private Formula formula;

    public GeocacheUserNote(long chatId, Geocache geocache) {
        setChatId(chatId);
        setGeocache(geocache);
        setId(generateId());
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public long getChatId() {
        return chatId;
    }

    private void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public Geocache getGeocache() {
        return geocache;
    }

    private void setGeocache(Geocache geocache) {
        //Sanity check
        if (geocache == null) throw new IllegalArgumentException("The geocache must not be null.");

        this.geocache = geocache;
    }

    public GeocacheUserStatus getStatus() {
        return status;
    }

    public GeocacheUserNote setStatus(GeocacheUserStatus status) {
        //Sanity check
        if (status == null) throw new IllegalArgumentException("The status must not be null.");

        this.status = status;
        return this;
    }

    public Formula getFormula() {
        return formula;
    }

    public GeocacheUserNote setFormula(Formula formula) {
        //Sanity check
        if (formula == null) throw new IllegalArgumentException("The formula must not be null.");

        this.formula = formula;
        return this;
    }

    private String generateId() {
        return String.format("%s/%s", chatId, geocache.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeocacheUserNote that = (GeocacheUserNote) o;
        return chatId == that.chatId && Objects.equals(geocache, that.geocache);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, geocache.getId());
    }
}
