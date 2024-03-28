package org.mslowko.turnbasedfighter.service;

import org.mslowko.turnbasedfighter.model.Unit;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UnitService {
    protected final Random random = new Random();

    public void attack(Unit source, Unit target) {
        int maxValue = source.getDamage();
        int value = random.nextInt(0, maxValue);
        target.setHp(target.getHp() - value);
    }
}
