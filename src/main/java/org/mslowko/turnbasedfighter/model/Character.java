package org.mslowko.turnbasedfighter.model;

import lombok.Getter;

@Getter
public class Character extends Unit {
    private int maxHP;
    private int maxHeal;

    public Character(String name) {
        super(name);
        this.currentHP = 20;
        this.damage = 10;
        this.maxHeal = 10;
        this.maxHP = currentHP;
    }
}
