package org.mslowko.turnbasedfighter.model;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document("players")
@RequiredArgsConstructor
public class Player {
    private final @MongoId String name;
}
