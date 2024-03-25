package org.mslowko.turnbasedfighter.pojo;

import lombok.Getter;
import org.apache.commons.lang3.mutable.MutableInt;

@Getter
public class Character extends Unit {
    private final MutableInt maxHP;
    private final MutableInt maxHeal;
    private final MutableInt level;

    public Character(String name) {
        super(name,
                new MutableInt(20),
                new MutableInt(10),
                new MutableInt(0));
        this.level = new MutableInt(0);
        this.maxHeal = new MutableInt(10);
        this.maxHP = hp;
    }
}
