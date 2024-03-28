package org.mslowko.turnbasedfighter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public abstract class Unit {
    protected int hp;
    protected int damage;
    protected int exp;
}
