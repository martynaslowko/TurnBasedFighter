package org.mslowko.turnbasedfighter.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.mutable.MutableInt;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public abstract class Unit {
    private final String name;
    protected MutableInt hp;
    protected MutableInt damage;
    protected MutableInt exp;
}
