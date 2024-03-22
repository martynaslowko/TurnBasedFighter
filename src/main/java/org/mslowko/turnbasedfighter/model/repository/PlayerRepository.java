package org.mslowko.turnbasedfighter.model.repository;

import org.mslowko.turnbasedfighter.model.Player;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlayerRepository extends MongoRepository<Player, String> {
}
