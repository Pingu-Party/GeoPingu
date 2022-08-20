package de.pinguparty.geopingu.worker.persistence;

import de.pinguparty.geopingu.worker.domain.GeocacheUserNote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/**
 * Repository for {@link GeocacheUserNote}s.
 */
@Repository
public interface GeocacheUserNoteRepository extends MongoRepository<GeocacheUserNote, String> {
    Set<GeocacheUserNote> findByChatId(long chatId);

    Optional<GeocacheUserNote> findByChatIdAndGeocache_Id(long chatId, String geocacheId);

    boolean existsByChatIdAndGeocache_Id(long chatId, String geocacheId);
}
