package org.mslowko.turnbasedfighter.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Node("Character")
public class Character extends Unit {
    @Id
    protected String name;
    @Version
    protected Long version;

    protected int maxHP;
    protected int maxHeal;
    protected int level;

    public Character(String name) {
        super(20,10,0);
        this.name = name;
        this.level = 0;
        this.maxHeal = 10;
        this.maxHP = hp;
    }

    public Character(int hp, int damage, int exp, String name, int level, int maxHeal, int maxHP, Long version) {
        super(hp, damage, exp);
        this.name = name;
        this.level = level;
        this.maxHeal = maxHeal;
        this.maxHP = maxHP;
        this.version = version;
    }
}
