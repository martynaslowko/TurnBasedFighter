package org.mslowko.turnbasedfighter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public abstract class Unit {
    private final String name;
    protected int hp;
    protected int damage;
    protected int exp;
}
