package org.mslowko.turnbasedfighter.service;

import lombok.RequiredArgsConstructor;
import org.mslowko.turnbasedfighter.pojo.Character;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacterService extends UnitService {
    public Character newCharacter(String name) {
        return new Character(name);
    }

    public void heal(Character character) {
        int maxValue = character.getMaxHeal().getValue();
        int minValue = maxValue - 10 + 1;
        int result = Math.min(random.nextInt(minValue, maxValue),
                character.getMaxHP().getValue());
        character.getHp().setValue(result);
    }
}
