package org.mslowko.turnbasedfighter.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Data
@Node("Player")
@NoArgsConstructor
public class Player {
    @Id
    private String name;

    private String password;

    @Relationship(type = "CREATED_BY", direction = Relationship.Direction.INCOMING)
    private List<Character> characters = new ArrayList<>();

    public Player(String name) {
        this.name = name;
    }
    public Player(String name, String password) {
        this.name = name;
        this.password = password;
    }

}
