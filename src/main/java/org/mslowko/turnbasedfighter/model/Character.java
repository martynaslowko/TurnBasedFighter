package org.mslowko.turnbasedfighter.model;

import lombok.Getter;

@Getter
public class Character extends Unit {
    private int maxHP;
    private int maxHeal;
    private int level;

    public Character(String name) {
        super(name, 20, 10, 0);
        this.maxHeal = 10;
        this.maxHP = hp;
        this.level = 0;
    }
}
