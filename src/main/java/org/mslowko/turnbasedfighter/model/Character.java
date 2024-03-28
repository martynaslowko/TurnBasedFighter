package org.mslowko.turnbasedfighter.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@EqualsAndHashCode(callSuper = true)
@Node("Character")
public class Character extends Unit {
    @Id
    protected String name;
    private int maxHP;
    private int maxHeal;
    private int level;

    public Character(String name) {
        super(20,10,0);
        this.name = name;
        this.level = 0;
        this.maxHeal = 10;
        this.maxHP = hp;
    }
}
