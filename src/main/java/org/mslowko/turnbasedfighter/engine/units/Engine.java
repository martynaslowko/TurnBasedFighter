package org.mslowko.turnbasedfighter.engine.units;

import org.mslowko.turnbasedfighter.model.Unit;

import java.util.Random;

public interface Engine<U> {
    Random random = new Random();

    default boolean isAlive(int value) {
        return value > 0;
    }

    U portToParent();

    default void attack(Unit target, int maxValue) {
        int value = random.nextInt(0, maxValue);
        target.setHp(target.getHp() - value);
    }
}
