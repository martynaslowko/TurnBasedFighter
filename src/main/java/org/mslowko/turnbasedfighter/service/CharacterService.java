package org.mslowko.turnbasedfighter.service;

import lombok.RequiredArgsConstructor;
import org.mslowko.turnbasedfighter.model.Character;
import org.mslowko.turnbasedfighter.model.repository.CharacterRepository;
import org.mslowko.turnbasedfighter.pojo.exceptions.CharacterNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CharacterService {
    private final CharacterRepository characterRepository;
    private final Random random = new Random();

    public Character newCharacter(String name) {
        return new Character(name);
    }

    public void heal(Character character) { //TODO
        int maxValue = character.getMaxHeal();
        int minValue = maxValue - 10 + 1;
        int result = Math.min(random.nextInt(minValue, maxValue),
                character.getMaxHP());
        character.setHp(result);
    }

    protected Character fetchCharacter(String id) {
        Optional<Character> optionalCharacter = characterRepository.findById(id);
        if (optionalCharacter.isEmpty())
            throw new CharacterNotFoundException(id);
        return optionalCharacter.get();
    }
}
