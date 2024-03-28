package org.mslowko.turnbasedfighter.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Data
@Node("Player")
public class Player {
    @Id
    private String name;

    @Relationship(type = "CREATED_BY", direction = Relationship.Direction.INCOMING)
    private List<Character> characters = new ArrayList<>();

    public Player(String name) {
        this.name = name;
    }
}
