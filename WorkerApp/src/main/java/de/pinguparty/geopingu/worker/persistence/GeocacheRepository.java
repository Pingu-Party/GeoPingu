package de.pinguparty.geopingu.worker.persistence;

import de.pinguparty.geopingu.worker.domain.geocache.Geocache;
import de.pinguparty.geopingu.worker.services.registry.InteractionRegistration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Geocache}s.
 */
@Repository
public interface GeocacheRepository extends MongoRepository<Geocache, String> {

}
