package org.mslowko.turnbasedfighter.service;

import lombok.RequiredArgsConstructor;
import org.mslowko.turnbasedfighter.model.Character;
import org.mslowko.turnbasedfighter.model.repository.CharacterRepository;
import org.mslowko.turnbasedfighter.pojo.exceptions.CharacterNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CharacterService {
    private final CharacterRepository characterRepository;

    public Character newCharacter(String name) {
        if (characterRepository.existsById(name))
            throw new IllegalArgumentException("Character already exists.");
        return new Character(name);
    }

    protected Character fetchCharacter(String id) {
        Optional<Character> optionalCharacter = characterRepository.findById(id);
        if (optionalCharacter.isEmpty())
            throw new CharacterNotFoundException(id);
        return optionalCharacter.get();
    }
}
