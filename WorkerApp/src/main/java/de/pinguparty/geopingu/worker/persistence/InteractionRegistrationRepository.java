package de.pinguparty.geopingu.worker.persistence;

import de.pinguparty.geopingu.worker.services.registry.InteractionRegistration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link InteractionRegistration}s, using their contained IDs of the affected chats
 * as unique identifiers.
 */
@Repository
public interface InteractionRegistrationRepository extends MongoRepository<InteractionRegistration, Long> {

}
