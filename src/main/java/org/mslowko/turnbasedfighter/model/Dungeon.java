package org.mslowko.turnbasedfighter.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.util.LinkedList;
import java.util.List;

@Data
@Node("Dungeon")
@RequiredArgsConstructor
public class Dungeon {
    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;
    private int currentWave;
    private final int waves;
    private final int slots;
    private boolean started;
    private String joinKey = RandomStringUtils.random(4, true, true).toUpperCase();

    @Relationship(type = "CURRENT_TURN", direction = Relationship.Direction.OUTGOING)
    private Character currentCharacter;

    @Relationship(type = "CURRENT_OPPONENT", direction = Relationship.Direction.OUTGOING)
    private Mob currentOpponent;

    @Relationship(type = "DEPLOYED_TO", direction = Relationship.Direction.INCOMING)
    private List<Character> lobby = new LinkedList<>();
}
