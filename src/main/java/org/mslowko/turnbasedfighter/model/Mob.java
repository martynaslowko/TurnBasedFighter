package org.mslowko.turnbasedfighter.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(force = true)
@Node("Mob")
public class Mob extends Unit {
    @Id
    @GeneratedValue
    protected String id;
    protected String name;
    protected String tier;

    public Mob(String name, String tier, int hp, int damage, int exp) {
        super(hp, damage, exp);
        this.name = name;
        this.tier = tier;
    }

    public Mob(String id, String name, String tier, int hp, int damage, int exp) {
        super(hp, damage, exp);
        this.id = id;
        this.name = name;
        this.tier = tier;
    }
}
