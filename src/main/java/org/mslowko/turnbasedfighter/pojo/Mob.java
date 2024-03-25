package org.mslowko.turnbasedfighter.pojo;

import org.apache.commons.lang3.mutable.MutableInt;

public class Mob extends Unit {
    private final String tier;
    public Mob(String name, String tier, int hp, int damage, int exp) {
        super(name,
                new MutableInt(hp),
                new MutableInt(damage),
                new MutableInt(exp));
        this.tier = tier;
    }
}
