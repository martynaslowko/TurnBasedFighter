package org.mslowko.turnbasedfighter.model.repository;

import org.mslowko.turnbasedfighter.model.Player;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface PlayerRepository extends Neo4jRepository<Player, String> {
}
