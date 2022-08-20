package de.pinguparty.geopingu.worker.services.geocache;

import de.pinguparty.geopingu.worker.domain.GeocacheUserNote;
import de.pinguparty.geopingu.worker.domain.geocache.Geocache;
import de.pinguparty.geopingu.worker.persistence.GeocacheRepository;
import de.pinguparty.geopingu.worker.persistence.GeocacheUserNoteRepository;
import de.pinguparty.geopingu.worker.services.fetching.GeocacheDetailsFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

/**
 * Component for managing {@link GeocacheUserNote} and the corresponding {@link Geocache}s.
 */
@Component
public class GeocacheManager {

    @Autowired
    private GeocacheUserNoteRepository geocacheUserNoteRepository;

    @Autowired
    private GeocacheRepository geocacheRepository;

    @Autowired
    private GeocacheDetailsFetcher geocacheDetailsFetcher;

    public GeocacheDetailsFetcher getDetailsFetcher() {
        return geocacheDetailsFetcher;
    }

    public Optional<Geocache> getGeocache(String geocacheId) {
        //Sanity check
        if ((geocacheId == null) || geocacheId.isEmpty())
            throw new IllegalArgumentException("The geocache ID must not be null or empty.");

        return geocacheRepository.findById(geocacheId);
    }

    public Geocache updateGeocache(Geocache geocache) {
        //Sanity checks
        if (geocache == null) throw new IllegalArgumentException("The geocache must not be null.");
        else if ((geocache.getId() == null) || geocache.getId().isEmpty())
            throw new IllegalArgumentException("The geocache ID must be present.");

        //Retrieve possibly already available data of this geocache
        Optional<Geocache> existingGeocacheData = geocacheRepository.findById(geocache.getId());

        //Check availability and whether this data is considered complete
        if (existingGeocacheData.isPresent() && existingGeocacheData.get().isComplete()) {
            return existingGeocacheData.get();
        }

        //Update geocache data in repository
        return geocacheRepository.save(geocache);
    }

    public Set<GeocacheUserNote> getGeocacheUserNotes(long chatId) {
        return geocacheUserNoteRepository.findByChatId(chatId);
    }

    public Optional<GeocacheUserNote> getGeocacheUserNote(long chatId, String geocacheId) {
        return geocacheUserNoteRepository.findByChatIdAndGeocache_Id(chatId, geocacheId);
    }

    public GeocacheUserNote updateGeocacheUserNote(GeocacheUserNote geocacheUserNote) {
        //Sanity check
        if (geocacheUserNote == null) throw new IllegalArgumentException("The geocache user note must not be null.");

        return geocacheUserNoteRepository.save(geocacheUserNote);
    }

    public void deleteGeocacheUserNote(long chatId, String geocacheId) {
        deleteGeocacheUserNote(new GeocacheUserNote(chatId, new Geocache(geocacheId)));
    }

    public void deleteGeocacheUserNote(GeocacheUserNote geocacheUserNote) {
        //Sanity check
        if (geocacheUserNote == null) throw new IllegalArgumentException("The geocache user note must not be null.");

        //Delete user note from repository
        geocacheUserNoteRepository.delete(geocacheUserNote);
    }
}
