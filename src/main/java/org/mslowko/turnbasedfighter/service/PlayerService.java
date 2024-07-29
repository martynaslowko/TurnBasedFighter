package org.mslowko.turnbasedfighter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.mslowko.turnbasedfighter.pojo.dto.CharacterDto;
import org.mslowko.turnbasedfighter.pojo.dto.PlayerDto;
import org.mslowko.turnbasedfighter.model.Character;
import org.mslowko.turnbasedfighter.model.Player;
import org.mslowko.turnbasedfighter.pojo.exceptions.CharacterNotFoundException;
import org.mslowko.turnbasedfighter.pojo.exceptions.PlayerNotFoundException;
import org.mslowko.turnbasedfighter.model.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final CharacterService characterService;
    private final ModelMapper modelMapper;

    public PlayerDto newPlayer(String name, String password) {
        if (playerRepository.existsById(name))
            throw new IllegalArgumentException("Player already exists.");

        Player player = new Player(name, password);
        playerRepository.save(player);
        log.info("New player has been created: {}", player.getName());
        return modelMapper.map(player, PlayerDto.class);
    }

    public PlayerDto getPlayer(String name) {
        return modelMapper.map(fetchPlayer(name), PlayerDto.class);
    }

    public PlayerDto addCharacter(String playerName, String characterName) {
        Player player = fetchPlayer(playerName);
        Character character = characterService.newCharacter(characterName);
        player.getCharacters().add(character);
        playerRepository.save(player);
        log.info("New character ({}) has been assigned to player {}.", character.getName(), player.getName());
        return modelMapper.map(player, PlayerDto.class);
    }

    public CharacterDto fetchPlayerCharacter(String playerName, String characterName) {
        Player player = fetchPlayer(playerName);
        Character character = characterService.fetchCharacter(characterName);
        if (!player.getCharacters().contains(character))
            throw new CharacterNotFoundException(characterName);
        return modelMapper.map(character, CharacterDto.class);
    }

    private Player fetchPlayer(String playerName) {
        Optional<Player> optionalPlayer = playerRepository.findById(playerName);
        if (optionalPlayer.isEmpty())
            throw new PlayerNotFoundException(playerName);
        return optionalPlayer.get();
    }
}
