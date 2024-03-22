package org.mslowko.turnbasedfighter.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class Unit {
    private final String name;
    protected int currentHP;
    protected int damage;
    protected int level;
    protected int exp;
}
