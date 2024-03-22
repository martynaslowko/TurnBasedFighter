package org.mslowko.turnbasedfighter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mslowko.turnbasedfighter.model.Character;
import org.mslowko.turnbasedfighter.model.Player;
import org.mslowko.turnbasedfighter.model.exceptions.PlayerNotFoundException;
import org.mslowko.turnbasedfighter.model.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final CharacterService characterService;

    public Player newPlayer(String name) {
        if (playerRepository.existsById(name))
            throw new IllegalArgumentException("Player already exists.");

        Player player = new Player(name);
        playerRepository.save(player);
        log.info("New player has been created: {}", player.getName());
        return player;
    }

    public Player addCharacter(String playerName, String characterName) {
        Optional<Player> optionalPlayer = playerRepository.findById(playerName);
        if (optionalPlayer.isEmpty())
            throw new PlayerNotFoundException(playerName);

        Character character = characterService.newCharacter(characterName);
        Player player = optionalPlayer.get();
        player.getCharacters().add(character);
        playerRepository.save(player);
        log.info("New character ({}) has been assigned to player {}.", character.getName(), player.getName());
        return player;
    }
}
