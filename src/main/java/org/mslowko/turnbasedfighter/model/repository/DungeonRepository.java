package org.mslowko.turnbasedfighter.model.repository;

import org.mslowko.turnbasedfighter.model.Dungeon;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.Optional;

public interface DungeonRepository extends Neo4jRepository<Dungeon, String> {
    @Query("MATCH (c:Character{name: $name})<-[DEPLOYED_TO]-(d:Dungeon) RETURN d")
    Optional<Dungeon> fetchDungeonByCharacterName(String name);
}
