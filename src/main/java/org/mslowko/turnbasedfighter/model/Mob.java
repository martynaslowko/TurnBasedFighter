package org.mslowko.turnbasedfighter.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@EqualsAndHashCode(callSuper = true)
@Node("Mob")
public class Mob extends Unit {
    @Id
    @GeneratedValue
    private String id;
    private final String name;
    private final String tier;
    public Mob(String name, String tier, int hp, int damage, int exp) {
        super(hp, damage, exp);
        this.name = name;
        this.tier = tier;
    }
}
