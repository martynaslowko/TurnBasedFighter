package org.mslowko.turnbasedfighter.model.repository;

import org.mslowko.turnbasedfighter.model.Mob;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface MobRepository  extends Neo4jRepository<Mob, String> {
}
