package org.mslowko.turnbasedfighter.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;
import java.util.List;

@Data
@Document("players")
public class Player {
    private @MongoId String name;
    private List<Character> characters = new ArrayList<>();

    public Player(String name) {
        this.name = name;
    }
}
