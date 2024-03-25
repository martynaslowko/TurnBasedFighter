package org.mslowko.turnbasedfighter.service;

import lombok.RequiredArgsConstructor;
import org.mslowko.turnbasedfighter.model.Character;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacterService extends UnitService {
    public Character newCharacter(String name) {
        return new Character(name);
    }

    public void heal(Character character) {
        int maxValue = character.getMaxHeal();
        int minValue = maxValue - 10 + 1;
        int value = character.getHp() + random.nextInt(minValue, maxValue);
        character.setHp(Math.min(value, character.getMaxHP()));
    }
}
