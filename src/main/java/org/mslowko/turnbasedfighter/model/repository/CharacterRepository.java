package org.mslowko.turnbasedfighter.model.repository;

import org.mslowko.turnbasedfighter.model.Character;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface CharacterRepository extends Neo4jRepository<Character, String> {
}
