package org.mslowko.turnbasedfighter.service;

import lombok.RequiredArgsConstructor;
import org.mslowko.turnbasedfighter.model.Character;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class CharacterService {
    private final Random random = new Random();

    public Character newCharacter(String name) {
        return new Character(name);
    }

    public void heal(Character character) {
        int maxValue = character.getMaxHeal();
        int minValue = maxValue - 10 + 1;
        int result = character.getCurrentHP() + random.nextInt(minValue, maxValue);
        character.setCurrentHP(Math.min(result, character.getMaxHP()));
    }
}
