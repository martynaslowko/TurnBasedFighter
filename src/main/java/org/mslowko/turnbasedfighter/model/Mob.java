package org.mslowko.turnbasedfighter.model;

public class Mob extends Unit{
    private final String tier;
    public Mob(String name, String tier, int hp, int damage, int exp) {
        super(name, hp, damage, exp);
        this.tier = tier;
    }
}
